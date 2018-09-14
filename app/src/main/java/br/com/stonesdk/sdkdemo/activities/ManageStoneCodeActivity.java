package br.com.stonesdk.sdkdemo.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.com.stonesdk.sdkdemo.R;
import stone.application.interfaces.StoneCallbackInterface;
import stone.providers.ActiveApplicationProvider;
import stone.user.UserModel;
import stone.utils.Stone;

/**
 * @author tiago.barbosa
 * @since 10/04/2018
 */
public class ManageStoneCodeActivity extends AppCompatActivity {

    private final List<UserModel> userModelList = Stone.sessionApplication.getUserModelList();
    private ListView stoneCodeListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_stone_code);

        findViewById(R.id.activateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateStoneCode();
            }
        });

        stoneCodeListView = findViewById(R.id.manageStoneCodeListView);
        stoneCodeListView.setAdapter(populateStoneCodeListView());
        stoneCodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                new AlertDialog.Builder(ManageStoneCodeActivity.this)
                        .setTitle("Desativar Stone Code")
                        .setMessage("Tem certeza que deseja desativar? Ao fazer isso, todas as transações feitas por esse stone code serão deletadas!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deactivateStoneCode(position);
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show()
                ;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        populateStoneCodeListView();
    }

    private ArrayAdapter populateStoneCodeListView() {
        ArrayAdapter<String> stoneCodeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        for (UserModel userModel : userModelList) {
            stoneCodeAdapter.add(userModel.getStoneCode());
        }
        return stoneCodeAdapter;
    }

    private void activateStoneCode() {
        final EditText stoneCodeEditText = findViewById(R.id.insertManageStoneCodeEditText);
        final ActiveApplicationProvider activeApplicationProvider = new ActiveApplicationProvider(ManageStoneCodeActivity.this);
        activeApplicationProvider.setDialogTitle("Aguarde");
        activeApplicationProvider.setDialogMessage("Ativando...");
        activeApplicationProvider.useDefaultUI(true);

        activeApplicationProvider.setConnectionCallback(new StoneCallbackInterface() {
            @Override
            public void onSuccess() {
                stoneCodeListView.setAdapter(populateStoneCodeListView());
                ((ArrayAdapter) stoneCodeListView.getAdapter()).notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError() {
                Toast.makeText(getApplicationContext(), "Erro: " + activeApplicationProvider.getListOfErrors(), Toast.LENGTH_LONG).show();
                Log.e("ManageStoneCodeActivity", "onError: " + activeApplicationProvider.getListOfErrors());
            }
        });

        activeApplicationProvider.activate(stoneCodeEditText.getText().toString());
    }

    private void deactivateStoneCode(int position) {
        final ActiveApplicationProvider activeApplicationProvider = new ActiveApplicationProvider(ManageStoneCodeActivity.this);
        activeApplicationProvider.setDialogTitle("Aguarde");
        activeApplicationProvider.setDialogMessage("Desativando...");
        activeApplicationProvider.useDefaultUI(true);

        activeApplicationProvider.setConnectionCallback(new StoneCallbackInterface() {
            @Override
            public void onSuccess() {
                stoneCodeListView.setAdapter(populateStoneCodeListView());
                ((ArrayAdapter) stoneCodeListView.getAdapter()).notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                if (!Stone.hasUserModel()) {
                    final Intent intent = new Intent(ManageStoneCodeActivity.this, ValidationActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onError() {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                Log.e("ManageStoneCodeActivity", "onError: " + activeApplicationProvider.getListOfErrors());
            }
        });
        activeApplicationProvider.deactivate(userModelList.get(position).getStoneCode());
    }

}
