package com.kalids.kmcontrol;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;
import java.text.NumberFormat;


import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    //declara variaveis concorrete
    private TextInputEditText ValorPneuNovoConcorrente;
    private TextInputEditText ValorPneuRecapadoConcorrente;
    private TextInputEditText KmPneuNovoConcorrente;
    private TextInputEditText KmPrimeiraRecapagemConcorrente;
    private TextView custoCpkConcorrente;

    // Declara local da moeda
    Locale localeBR = new Locale("pt","BR");

    //declara variaveis bandag
    private TextInputEditText ValorPneuNovoBandag;
    private TextInputEditText ValorPneuRecapadoBandag;
    private TextInputEditText KmPneuNovoBandag;
    private TextInputEditText KmPneuRecapadoBandag;
    private TextView custoCpkBandag;

    //variaveis de comparacao
    private TextInputEditText KmRodadoMes;
    private SeekBar seekBarMeses;
    private TextView textViewCustoConcorrente;
    private TextView textViewCustoBandag;
    private TextView textViewQuantidadeMeses;

    //variaveis Global
    private double cpkConcorrenteComparar;
    private double cpkBandagComparar;
    private double custoBandagComparar;
    private double custoConcorrenteComparar;

    //variaveis custo total
    private SeekBar seekBarQuantidadePneus;
    private SeekBar seekBarQuantidadeVeiculos;
    private TextView textViewCustoTotalConcorrencia;
    private TextView textViewCustoTotalBandag;
    private TextView textViewResultadoFinal;
    private TextView textViewQuantidadePneusDisplay;
    private TextView textViewQuantidadeVeiculosDisplay;

    //configura formatacao em dinheiro
    NumberFormat dinheiro = NumberFormat.getCurrencyInstance(localeBR);

    DecimalFormat Cpk = new DecimalFormat("0.0000");
    DecimalFormat KmReal = new DecimalFormat("0.0");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //oculta a barra com o nome do app
        getSupportActionBar().hide();

        //seta variaveis concorrente
        ValorPneuNovoConcorrente  = findViewById(R.id.ValorPneuNovoConcorrente);
        ValorPneuRecapadoConcorrente = findViewById(R.id.ValorPneuRecapadoConcorrente);
        KmPneuNovoConcorrente = findViewById(R.id.KmPneuNovoConcorrente);
        KmPrimeiraRecapagemConcorrente = findViewById(R.id.KmPrimeiraRecapagemConcorrente);
        custoCpkConcorrente = findViewById(R.id.custoCpkConcorrente);

        // seta variaveis bandag
        ValorPneuNovoBandag  = findViewById(R.id.ValorPneuNovoBandag);
        ValorPneuRecapadoBandag = findViewById(R.id.ValorPneuRecapadoBandag);
        KmPneuNovoBandag = findViewById(R.id.KmPneuNovoBandag);
        KmPneuRecapadoBandag = findViewById(R.id.KmPneuRecapadoBandag);
        custoCpkBandag = findViewById(R.id.custoCpkBandag);

        // seta variaveis comparacao
        KmRodadoMes = findViewById(R.id.KmRodadoMes);
        seekBarMeses = findViewById(R.id.seekBarMeses);
        textViewCustoConcorrente = findViewById(R.id.textViewCustoConcorrencia);
        textViewCustoBandag = findViewById(R.id.textViewCustoBandag);
        textViewQuantidadeMeses = findViewById(R.id.textViewQuantidadeMeses);

        //seta variaveis custo total
        seekBarQuantidadePneus = findViewById(R.id.seekBarQuantidadePneus);
        seekBarQuantidadeVeiculos = findViewById(R.id.seekBarQuantidadeVeiculos);
        textViewCustoTotalConcorrencia = findViewById(R.id.textViewCustoTotalConcorrencia);
        textViewCustoTotalBandag = findViewById(R.id.textViewCustoTotalBandag);
        textViewResultadoFinal = findViewById(R.id.textViewResultadoFinal);
        textViewQuantidadePneusDisplay = findViewById(R.id.textViewQuantidadePneusDisplay);
        textViewQuantidadeVeiculosDisplay = findViewById(R.id.textViewQuantidadeVeiculosDisplay);

        //configura seekBar Meses
        seekBarMeses.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String kmrodado = KmRodadoMes.getText().toString();
                Boolean validaCamposComparacao =  validaCamposComparacao(kmrodado);

                textViewQuantidadeMeses.setText(progress + " mes(es)");

                if(validaCamposComparacao){
                    double totalRodado = Double.parseDouble(kmrodado);
                    int pneusPorVeiculos = seekBarQuantidadePneus.getProgress();
                    int totalVeiculos = seekBarQuantidadeVeiculos.getProgress();
                    int totalPneus = pneusPorVeiculos * totalVeiculos;

                    //custo Concorrente
                    double cpkConcorrente = cpkConcorrenteComparar;
                    double custoConcorrente = totalRodado * cpkConcorrente;
                    custoConcorrente =  custoConcorrente * progress;
                    custoConcorrenteComparar = custoConcorrente;
                    textViewCustoConcorrente.setText(dinheiro.format(custoConcorrente));

                    //custo total concorrente
                    double custoTotalConcorrente = totalPneus * custoConcorrente;
                    textViewCustoTotalConcorrencia.setText(dinheiro.format(custoTotalConcorrente));

                    //custo Bandag
                    double cpkBandag = cpkBandagComparar;
                    double custoBandag = totalRodado * cpkBandag;
                    custoBandag = custoBandag * progress;
                    custoBandagComparar = custoBandag;
                    textViewCustoBandag.setText(dinheiro.format(custoBandag));

                    //custo total Bandag
                    double custoTotalBandag = totalPneus * custoBandag;
                    textViewCustoTotalBandag.setText(dinheiro.format(custoTotalBandag));

                    //preenche o resultado final
                    if(custoTotalBandag > custoTotalConcorrente){
                        double lucro = custoTotalBandag - custoTotalConcorrente;
                        textViewResultadoFinal.setText("Veja só! você vai economizar "+dinheiro.format(lucro)+" comprando na Concorrência");
                    }
                    else if(custoTotalBandag < custoTotalConcorrente){
                        double lucro = custoTotalConcorrente - custoTotalBandag;
                        textViewResultadoFinal.setText("Veja só! você vai economizar "+dinheiro.format(lucro)+" comprando na Bandag");

                    }else{
                        textViewResultadoFinal.setText("Ambas as empresas estão com o mesmo custo/beneficio");
                    }

                }
                else{
                    Toast.makeText(
                            getApplicationContext(),
                            "Ops!! Precisa me dizer o quando você roda no mês",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //configura seekbar pneus por veiculo
        seekBarQuantidadePneus.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                textViewQuantidadePneusDisplay.setText(""+progress);
                String kmrodado = KmRodadoMes.getText().toString();
                Boolean validaCamposComparacao =  validaCamposComparacao(kmrodado);

                if(validaCamposComparacao){
                    double totalRodado = Double.parseDouble(kmrodado);
                    int quantidadeMeses = seekBarMeses.getProgress();
                    int totalVeiculos = seekBarQuantidadeVeiculos.getProgress();
                    int totalPneus = totalVeiculos * progress;

                    //calcula custo concorrente
                    double cpkConcorrente = cpkConcorrenteComparar;
                    double custoConcorrente = totalRodado * cpkConcorrente;
                    textViewCustoConcorrente.setText(dinheiro.format(custoConcorrente*quantidadeMeses));
                    double custoTotalConcorrente = custoConcorrente * totalPneus;
                    custoTotalConcorrente = custoTotalConcorrente * quantidadeMeses;
                    textViewCustoTotalConcorrencia.setText(dinheiro.format(custoTotalConcorrente));

                    //calcula custo bandag
                    double cpkbandag = cpkBandagComparar;
                    double custobandag = totalRodado * cpkbandag;
                    textViewCustoBandag.setText(dinheiro.format(custobandag*quantidadeMeses));
                    double custoTotalBandag = custobandag * totalPneus;
                    custoTotalBandag = custoTotalBandag * quantidadeMeses;
                    textViewCustoTotalBandag.setText(dinheiro.format(custoTotalBandag));

                    //faz comparativos
                    if(custoTotalBandag > custoTotalConcorrente){
                        double lucro = custoTotalBandag - custoTotalConcorrente;
                        textViewResultadoFinal.setText("Veja só! você vai economizar "+dinheiro.format(lucro)+" comprando na Concorrência");
                    }else if(custoTotalBandag < custoTotalConcorrente){
                        double lucro = custoTotalConcorrente - custoTotalBandag;
                        textViewResultadoFinal.setText("Veja só! você vai economizar "+dinheiro.format(lucro)+" comprando na Bandag");

                    }else{
                        textViewResultadoFinal.setText("Ambas as empresas estão com o mesmo custo/beneficio");
                    }
                }
                else{
                    Toast.makeText(
                            getApplicationContext(),
                            "Ops!! Precisa me dizer o quando você roda no mês",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //configura seekBar total de veiculos
        seekBarQuantidadeVeiculos.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                textViewQuantidadeVeiculosDisplay.setText(""+progress);
                String kmrodado = KmRodadoMes.getText().toString();
                Boolean validaCamposComparacao =  validaCamposComparacao(kmrodado);

                if(validaCamposComparacao){
                    double totalRodado = Double.parseDouble(kmrodado);
                    int quantidadeMeses = seekBarMeses.getProgress();
                    int totalPneus =  seekBarQuantidadePneus.getProgress() * progress;

                    //calcula custo concorrente
                    double cpkConcorrente = cpkConcorrenteComparar;
                    double custoConcorrente = totalRodado * cpkConcorrente;
                    textViewCustoConcorrente.setText(dinheiro.format(custoConcorrente*quantidadeMeses));
                    double custoTotalConcorrente = custoConcorrente * totalPneus;
                    custoTotalConcorrente = custoTotalConcorrente * quantidadeMeses;
                    textViewCustoTotalConcorrencia.setText(dinheiro.format(custoTotalConcorrente));

                    //calcula custo bandag
                    double cpkbandag = cpkBandagComparar;
                    double custobandag = totalRodado * cpkbandag;
                    textViewCustoBandag.setText(dinheiro.format(custobandag*quantidadeMeses));
                    double custoTotalBandag = custobandag * totalPneus;
                    custoTotalBandag = custoTotalBandag * quantidadeMeses;
                    textViewCustoTotalBandag.setText(dinheiro.format(custoTotalBandag));

                    //faz comparativos
                    if(custoTotalBandag > custoTotalConcorrente){
                        double lucro = custoTotalBandag - custoTotalConcorrente;
                        textViewResultadoFinal.setText("Veja só! você vai economizar "+dinheiro.format(lucro)+" comprando na Concorrência");
                    }else if(custoTotalBandag < custoTotalConcorrente){
                        double lucro = custoTotalConcorrente - custoTotalBandag;
                        textViewResultadoFinal.setText("Veja só! você vai economizar "+dinheiro.format(lucro)+" comprando na Bandag");

                    }else{
                        textViewResultadoFinal.setText("Ambas as empresas estão com o mesmo custo/beneficio");
                    }
                }
                else{
                    Toast.makeText(
                            getApplicationContext(),
                            "Ops!! Precisa me dizer o quando você roda no mês",
                            Toast.LENGTH_SHORT
                    ).show();
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    public void calculaCpkConcorrente(View view){
        String p1 = ValorPneuNovoConcorrente.getText().toString();
        String p2 = ValorPneuRecapadoConcorrente.getText().toString();
        String k1 = KmPneuNovoConcorrente.getText().toString();
        String k2 = KmPrimeiraRecapagemConcorrente.getText().toString();
        Boolean validaCamposConcorrente =  validaCamposConcorrete(p1,p2,k1,k2);

        if(validaCamposConcorrente){
            double precoPneuNovoConcorrete = Double.parseDouble(ValorPneuNovoConcorrente.getText().toString());
            double precoPneuRecapadoConcorrete = Double.parseDouble(ValorPneuRecapadoConcorrente.getText().toString());
            double kmPneuNovoConcorrete = Double.parseDouble(KmPneuNovoConcorrente.getText().toString());
            double kmPneuRecapadoConcorrete = Double.parseDouble(KmPrimeiraRecapagemConcorrente.getText().toString());

            double kmTotalConcorrente = kmPneuNovoConcorrete + kmPneuRecapadoConcorrete;
            double precoTotalConcorrente = precoPneuNovoConcorrete + precoPneuRecapadoConcorrete;
            double cpkConcorrente = precoTotalConcorrente / kmTotalConcorrente  ;
            cpkConcorrenteComparar = cpkConcorrente;

            custoCpkConcorrente.setText("CPK : "+ Cpk.format(cpkConcorrente));
            //calcula km para custar 1 real
            double kmPorUmReal = 1.00 / cpkConcorrente;

            //alert dialog
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Olha só!");
            dialog.setMessage("Você precisaria rodar " + KmReal.format(kmPorUmReal)+ " km para custar R$1,00 cada pneu.");
            dialog.setPositiveButton("Entendi", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.create();
            dialog.show();
            //fim dialog

        } else{
            Toast.makeText(
                    getApplicationContext(),
                    "Por favor preencha todos os campos",
                    Toast.LENGTH_SHORT
            ).show();
        }

    }

    public void calculaCpkBandag(View view){
        String p1 = ValorPneuNovoBandag.getText().toString();
        String p2 = ValorPneuRecapadoBandag.getText().toString();
        String k1 = KmPneuNovoBandag.getText().toString();
        String k2 = KmPneuRecapadoBandag.getText().toString();
        Boolean validaCamposConcorrente =  validaCamposConcorrete(p1,p2,k1,k2);

        if(validaCamposConcorrente){
            double precoPneuNovoBandag = Double.parseDouble(ValorPneuNovoBandag.getText().toString());
            double precoPneuRecapadoBandag = Double.parseDouble(ValorPneuRecapadoBandag.getText().toString());
            double kmPneuNovoBandag = Double.parseDouble(KmPneuNovoBandag.getText().toString());
            double kmPneuRecapadoBandag = Double.parseDouble(KmPneuRecapadoBandag.getText().toString());


            double kmTotalBandag = kmPneuNovoBandag + kmPneuRecapadoBandag;
            double precoTotalBandag = precoPneuNovoBandag + precoPneuRecapadoBandag;
            double cpkBandag = precoTotalBandag / kmTotalBandag  ;
            cpkBandagComparar = cpkBandag;


            custoCpkBandag.setText("CPK : "+ Cpk.format(cpkBandag));

            //calcula km para custar 1 real
            double kmPorUmReal = 1.00 / cpkBandag;

            //alert dialog
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Olha só!");
            dialog.setMessage("Você precisaria rodar " + KmReal.format(kmPorUmReal)+ " km para custar R$1,00 cada pneu.");
            dialog.setPositiveButton("Entendi", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.create();
            dialog.show();
            //fim dialog


        } else{
            Toast.makeText(
                    getApplicationContext(),
                    "Por favor preencha todos os campos",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    public Boolean validaCamposConcorrete(String p1, String p2, String k1, String k2){
        Boolean camposValidados = true;

        if(p1 == null || p1.equals("") || p2 == null || p2.equals("") || k1 == null || k1.equals("") || k2 == null || k2.equals("")){
            camposValidados =  false;
        }
        return camposValidados;
    }

    public Boolean validaCamposComparacao(String k1){
        Boolean camposValidados = true;
        if (k1 == null || k1.equals("")){
            camposValidados = false;
        }
        return  camposValidados;
    }

}
