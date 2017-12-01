package com.example.rodrigo.at_monetizacao_rodrigo_filomeno;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.ConfirmEmail;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Digits;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ValidationListener {

    Validator validator;
    InterstitialAd anuncio;

    @NotEmpty(message = "Campo não pode ficar em branco")
    private EditText Nome;

    @NotEmpty(message = "Campo não pode ficar em branco")
    @Email(message = "coloque um email valido")
    private EditText Email;

    @NotEmpty(message = "Campo não pode ficar em branco")
    @Password(min = 6, message = "minimo de 6 caracteres")
    private EditText Password;

    @NotEmpty(message = "Campo não pode ficar em branco")
    @ConfirmPassword(message = "Senhas devem ser iguais")
    private EditText ConfirmPassword;
    @NotEmpty(message = "Campo não pode ficar em branco")
    @Length(min = 14, max = 14, message = "coloque um CPF valido")
    private EditText Cpf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        validator = new Validator(this);
        validator.setValidationListener(this);


        anuncio = new InterstitialAd(this);
        //chave do admob
        anuncio.setAdUnitId("ca-app-pub-2048265031968487/7982707743");

        anuncio.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                Toast.makeText(MainActivity.this, " Obrigado !", Toast.LENGTH_SHORT).show();
                //limpar os campos edit text
                Nome.setText("");
                Email.setText("");
                Password.setText("");
                ConfirmPassword.setText("");
                Cpf.setText("");


            }
        });


        Nome = (EditText) findViewById(R.id.EtNome);
        Email = (EditText) findViewById(R.id.EtEmail);
        Password = (EditText) findViewById(R.id.EtSenha);
        ConfirmPassword = (EditText) findViewById(R.id.EtConfSenha);
        Cpf = (EditText) findViewById(R.id.EtCpf);

        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher mtw = new MaskTextWatcher(Cpf, smf);
        Cpf.addTextChangedListener(mtw);

    }
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
              /*  .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")*/
                .build();

        anuncio.loadAd(adRequest);
    }

    public void Salvar(View v){
        validator.validate();

    }

    @Override
    public void onValidationSucceeded() {
        anuncio.show();
        Mensagem("Campos Validados com Sucesso!");
        SalvarNoArquivo();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Mensagem(message);
            }
        }
    }

    private void Mensagem (String msg){

        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

    }

    public void SalvarNoArquivo (){

        Contato contato = new Contato(Nome.getText().toString(),Email.getText().toString(),Password.getText().toString(),ConfirmPassword.getText().toString(),Cpf.getText().toString());

        try{
            FileOutputStream fos = openFileOutput("contatos.txt", Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(contato);

            Mensagem("Contato salvo com sucesso!");


        }catch (Exception e){

            Mensagem("Erro : " + e.getMessage());

        }


    }
}
