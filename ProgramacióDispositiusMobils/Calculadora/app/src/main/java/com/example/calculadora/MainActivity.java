package com.example.calculadora;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> llistaNumeros = new ArrayList<>();
    List<String> llistaOperadors = new ArrayList<>();
    String num ="";
    String operacioActual ="";
    boolean acabatDeCalcular = false;

    private void reiniciar() {
        llistaNumeros.clear();
        llistaOperadors.clear();
        num = "";
        operacioActual = "";
        acabatDeCalcular = false;
    }

    private void novaOperacioSiCal() {
        if (acabatDeCalcular) {
            reiniciar();
        }
    }

    private String formatear(double valor) {
        if (valor == (long) valor) {
            return String.valueOf((long) valor);
        }
        return String.valueOf(valor);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView txtResultat = findViewById(R.id.txtView_Resultat);
        Button btn_0 = findViewById(R.id.btn_0);
        Button btn_1 = findViewById(R.id.btn_1);
        Button btn_2 = findViewById(R.id.btn_2);
        Button btn_3 = findViewById(R.id.bnt_3);
        Button btn_4 = findViewById(R.id.btn_4);
        Button btn_5 = findViewById(R.id.btn_5);
        Button btn_6 = findViewById(R.id.btn_6);
        Button btn_7 = findViewById(R.id.btn_7);
        Button btn_8 = findViewById(R.id.btn_8);
        Button btn_9 = findViewById(R.id.btn_9);
        Button btn_suma = findViewById(R.id.btn_suma);
        Button btn_resta = findViewById(R.id.bnt_resta);
        Button btn_multiplicacio = findViewById(R.id.btn_multuplicacio);
        Button btn_divisio = findViewById(R.id.btn_divisio);
        Button btn_resultat = findViewById(R.id.btn_Resultat);
        Button btn_decimal = findViewById(R.id.btn_Decimal);
        Button btn_Borrar = findViewById(R.id.btn_borrar);
        Button btn_C = findViewById(R.id.btn_C);
        Button btn_CE = findViewById(R.id.btn_CE);

        btn_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                novaOperacioSiCal();    // NUEVO
                if (!num.equals("0"))
                {
                    num+="0";
                    txtResultat.setText(operacioActual+="0");
                }
            }
        });
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                novaOperacioSiCal();
                num+="1";
                txtResultat.setText(operacioActual+="1");
            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                novaOperacioSiCal();
                num+="2";
                txtResultat.setText(operacioActual+="2");
            }
        });
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                novaOperacioSiCal();
                num+="3";
                txtResultat.setText(operacioActual+="3");
            }
        });
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                novaOperacioSiCal();
                num+="4";
                txtResultat.setText(operacioActual+="4");
            }
        });
        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                novaOperacioSiCal();
                num+="5";
                txtResultat.setText(operacioActual+="5");
            }
        });
        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                novaOperacioSiCal();
                num+="6";
                txtResultat.setText(operacioActual+="6");
            }
        });
        btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                novaOperacioSiCal();
                num+="7";
                txtResultat.setText(operacioActual+="7");
            }
        });
        btn_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                novaOperacioSiCal();
                num+="8";
                txtResultat.setText(operacioActual+="8");
            }
        });
        btn_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                novaOperacioSiCal();
                num+="9";
                txtResultat.setText(operacioActual+="9");
            }
        });

        btn_decimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                novaOperacioSiCal();
                if (num.equals(""))
                {
                    num = "0.";
                    txtResultat.setText(operacioActual+="0.");
                }
                else if (!num.contains("."))
                {
                    num += ".";
                    txtResultat.setText(operacioActual+=".");
                }
            }
        });

        btn_suma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acabatDeCalcular = false;
                if (!num.equals(""))
                {
                    llistaNumeros.add(num);
                    num="";
                    llistaOperadors.add("+");
                    txtResultat.setText(operacioActual+="+");
                }
                else if (!operacioActual.equals(""))
                {
                    operacioActual = operacioActual.substring(0, operacioActual.length() - 1);
                    txtResultat.setText(operacioActual);
                    txtResultat.setText(operacioActual+="+");
                    llistaOperadors.remove(llistaOperadors.size()-1);
                    llistaOperadors.add("+");
                }
            }
        });
        btn_resta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acabatDeCalcular = false;
                if (!num.equals(""))
                {
                    llistaNumeros.add(num);
                    num="";
                    llistaOperadors.add("-");
                    txtResultat.setText(operacioActual+="-");
                }
                else if (!operacioActual.equals(""))
                {
                    operacioActual = operacioActual.substring(0, operacioActual.length() - 1);
                    txtResultat.setText(operacioActual);
                    txtResultat.setText(operacioActual+="-");
                    llistaOperadors.remove(llistaOperadors.size()-1);
                    llistaOperadors.add("-");
                }
            }
        });
        btn_multiplicacio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acabatDeCalcular = false;
                if (!num.equals(""))
                {
                    llistaNumeros.add(num);
                    num="";
                    llistaOperadors.add("*");
                    txtResultat.setText(operacioActual+="*");
                }
                else if (!operacioActual.equals(""))
                {
                    operacioActual = operacioActual.substring(0, operacioActual.length() - 1);
                    txtResultat.setText(operacioActual);
                    txtResultat.setText(operacioActual+="*");
                    llistaOperadors.remove(llistaOperadors.size()-1);
                    llistaOperadors.add("*");
                }
            }
        });
        btn_divisio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acabatDeCalcular = false;
                if (!num.equals(""))
                {
                    llistaNumeros.add(num);
                    num="";
                    llistaOperadors.add("/");
                    txtResultat.setText(operacioActual+="/");
                }
                else if (!operacioActual.equals(""))
                {
                    operacioActual = operacioActual.substring(0, operacioActual.length() - 1);
                    txtResultat.setText(operacioActual);
                    txtResultat.setText(operacioActual+="/");
                    llistaOperadors.remove(llistaOperadors.size()-1);
                    llistaOperadors.add("/");
                }
            }
        });

        btn_C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reiniciar();
                txtResultat.setText("0");
            }
        });

        btn_CE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acabatDeCalcular = false;
                operacioActual = operacioActual.substring(0, operacioActual.length() - num.length());
                num = "";
                txtResultat.setText(operacioActual.equals("") ? "0" : operacioActual);
            }
        });

        btn_Borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acabatDeCalcular = false;
                if (operacioActual.equals(""))
                {
                    return;
                }
                operacioActual = operacioActual.substring(0, operacioActual.length() - 1);
                if (!num.equals(""))
                {
                    num = num.substring(0, num.length() - 1);
                }
                else
                {
                    llistaOperadors.remove(llistaOperadors.size()-1);
                    num = llistaNumeros.remove(llistaNumeros.size()-1);
                }
                txtResultat.setText(operacioActual.equals("") ? "0" : operacioActual);
            }
        });

        btn_resultat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double resultat=0;
                if (!num.equals(""))
                {
                    llistaNumeros.add(num);
                    num="";
                }
                if (llistaNumeros.isEmpty())
                {
                    return;
                }
                if (llistaOperadors.size() >= llistaNumeros.size())
                {
                    llistaOperadors.remove(llistaOperadors.size()-1);
                }

                for (int i = 0; i < llistaOperadors.size(); i++) {
                    String operador = llistaOperadors.get(i);

                    if (operador.equals("*")||operador.equals("/"))
                    {
                        double num1 = Double.parseDouble(llistaNumeros.get(i));
                        double num2 = Double.parseDouble(llistaNumeros.get(i + 1));

                        double resultado=0;

                        if (operador.equals("*")) {
                            resultado = num1 * num2;
                        } else {
                            if (num2 == 0)
                            {
                                reiniciar();
                                txtResultat.setText("Error");
                                return;
                            }
                            resultado = num1 / num2;
                        }
                        llistaNumeros.set(i, String.valueOf(resultado));

                        llistaNumeros.remove(i + 1);

                        llistaOperadors.remove(i);

                        i--;
                    }
                }
                while(!llistaNumeros.isEmpty() && !llistaOperadors.isEmpty())
                {
                    double num1 = Double.parseDouble(llistaNumeros.get(0));
                    llistaNumeros.remove(0);
                    double num2 = Double.parseDouble(llistaNumeros.get(0));
                    llistaNumeros.remove(0);

                    String operador = llistaOperadors.get(0);
                    llistaOperadors.remove(0);

                    if (operador.equals("+"))
                    {
                        resultat=num1+num2;
                    } else if (operador.equals("-")) {
                        resultat=num1-num2;
                    }
                    llistaNumeros.add(0, String.valueOf(resultat));
                }
                resultat = Double.parseDouble(llistaNumeros.get(0));

                String textoResultat = formatear(resultat);
                llistaNumeros.clear();
                llistaOperadors.clear();
                num = textoResultat;
                operacioActual = textoResultat;
                txtResultat.setText(textoResultat);
                acabatDeCalcular = true;
            }
        });
    }
}