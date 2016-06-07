package pt.ipleiria.listademusica;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> albums;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sp = getSharedPreferences("listademusica", 0);
        Set<String> contactsSet = sp.getStringSet("albumsKey", new HashSet<String>());

    albums = new ArrayList<String>();

    albums.add("Linkin Park - Meteora - 2003");
    albums.add("Crown The Empire - Rise of the Runaways - 2014");
    albums.add("Hands Like Houses - Dissonants - 2016");
    albums.add("Asking Alexandria - From Death to Destiny - 2013");
    albums.add("This Wild Life - Clouded - 2014");
    albums.add("Metallica - Death Magnetic - 2008");
    albums.add("Pablo Alboran - Tour Terral - 2015");
    albums.add("Black Veil Brides - Set The World On Fire - 2011");

    //Spinner
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, albums);

    ListView listView = (ListView) findViewById(R.id.listView_album);
    listView.setAdapter(adapter);

    Spinner spinner = (Spinner) findViewById(R.id.spinner_search);

    // Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<CharSequence> adapterS = ArrayAdapter.createFromResource(this,
            R.array.spinner_option, android.R.layout.simple_spinner_item);
    // Specify the layout to use when the list of choices appears
    adapterS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
    spinner.setAdapter(adapterS);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //colocado o codigo a executar quando se clica num item da listview



                ListView listView = (ListView) findViewById(R.id.listView_albums);

                String item = (String) listView.getItemAtPosition(position);

                albums.remove(position);


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        MainActivity.this, android.R.layout.simple_list_item_1, albums);
                listView.setAdapter(adapter);

                Toast.makeText(MainActivity.this, "Apagou o album: " + item, Toast.LENGTH_SHORT).show();

            }
        });
}

    @Override
    protected void onStop() {
        super.onStop();

        Toast.makeText(MainActivity.this, "A guardar dados.", Toast.LENGTH_SHORT).show();

        //guardar os contactos para a shared preferences
        SharedPreferences sp = getSharedPreferences("listademusica", 0);
        SharedPreferences.Editor editor = sp.edit();
        HashSet contactsSet = new HashSet(albums);
        editor.putStringSet("albumsKey", contactsSet);
        editor.commit();

    }


    private SimpleAdapter createSimpleAdapter(ArrayList<String> contacts) {
        List<HashMap<String, String>> simpleAdapterData = new ArrayList<HashMap<String, String>>();

        for (String c : contacts) {
            HashMap<String, String> hashMap = new HashMap<>();

            String[] split = c.split(" \\| ");

            hashMap.put("Tudo", split[0]);
            hashMap.put("Artista", split[1]);
            hashMap.put("Ano de Lançamento", split[2]);
            hashMap.put("Avaliação", split[3]);
            hashMap.put("Editora", split[4]);

            simpleAdapterData.add(hashMap);
        }

        String[] from = {"Tudo", "Artista", "Ano de Lançamento", "Avaliação", "Editora"};
        int[] to = {R.id.textView_artista, R.id.textView_ano};
        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), simpleAdapterData, R.layout.listview_item, from, to);
        return simpleAdapter;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick_SearchAlbum (View view) {

        //ir buscar referência para a edittest, spinner e listview
        EditText et = (EditText) findViewById(R.id.editText_search);
        Spinner sp = (Spinner) findViewById(R.id.spinner_search);
        ListView lv = (ListView) findViewById(R.id.listView_album);


        // ir á edittest buscar o termo a pesquisar
        String termo = et.getText().toString();

        if (termo.equals("")) { //se o termo a pesquisar for uma string vazia
            //mostra os conctactos todos
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, albums);
            lv.setAdapter(adapter);

            Toast.makeText(MainActivity.this, "A mostrar resultados da pesquisa.", Toast.LENGTH_SHORT).show();

        } else {


            String itemSeleccionado = (String) sp.getSelectedItem();


            //pesquisar o termo nos albums, e guardar o resultado da pesquisa numa lista nova


            ArrayList<String> resultados = new ArrayList<>();


            if (itemSeleccionado.equals("Tudo")) {
                for (int i = 0; i < albums.size(); i++) {
                    String c = albums.get(i);

            //                String[] s = c.split("\\|");
            //                String name = s[0];
            //
            //                boolean contem = name.contains(termo);
            //
            //                if(contem)
            //                    resultados.add(c);

                    boolean contem = c.contains(termo);

                    if (contem == true) {
                        resultados.add(c);
                    }
                }
            } else if (itemSeleccionado.equals("Artista")) {

                for (int i = 0; i < albums.size(); i++) {
                    String c = albums.get(i);

                    String[] s = c.split("\\-");
                    String artista = s[0];

                    boolean contem = artista.contains(termo);

                    if (contem) {
                        resultados.add(c);
                    }
                }
            }  else if (itemSeleccionado.equals("Album")) {


                for (int i = 0; i < albums.size(); i++) {
                    String c = albums.get(i);

                    String[] s = c.split("\\-");
                    String alb = s[1];

                    boolean contem = alb.contains(termo);

                    if (contem) {
                        resultados.add(c);
                    }
                }

            }  else if (itemSeleccionado.equals("Ano de Lançamento")) {


                    for (int i = 0; i < albums.size(); i++) {
                        String c = albums.get(i);

                        String[] s = c.split("\\-");
                        String ano = s[2];

                        boolean contem = ano.contains(termo);

                        if (contem) {
                            resultados.add(c);
                        }
                    }

            }  else if (itemSeleccionado.equals("Editora")) {


                for (int i = 0; i < albums.size(); i++) {
                    String c = albums.get(i);

                    String[] s = c.split("\\|");
                    String ano = s[3];

                    boolean contem = ano.contains(termo);

                    if (contem) {
                        resultados.add(c);
                    }
                }

            } else if (itemSeleccionado.equals("Avaliação")) {

                for (int i = 0; i < albums.size(); i++) {
                    String c = albums.get(i);

                    String[] s = c.split("\\|");
                    String aval = s[4];

                    boolean contem = aval.contains(termo);

                    if (contem) {
                        resultados.add(c);
                    }
                }

            }

            boolean vazia = resultados.isEmpty();

            if (vazia == false) {


                //mostrar na listview a lista nova q contém o resultado da pesquisa

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        this, android.R.layout.simple_list_item_1, resultados);
                lv.setAdapter(adapter);

                //mostrar uma mensagem a dizer que a pesquisa teve sucesso
                Toast.makeText(MainActivity.this, "A mostrar resultados da pesquisa.", Toast.LENGTH_SHORT).show();
            } else { //se a lista está vazia
                //mostrar os albums todos

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1, albums);
                lv.setAdapter(adapter);

                Toast.makeText(MainActivity.this, "Resultados não encontrados. A mostrar resultados da pesquisa.", Toast.LENGTH_SHORT).show();



            }
        }
    }


    public void onClick_addAlbum (View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);



        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View inflate = inflater.inflate(R.layout.dialog_add, null);
        builder.setView(inflate);


        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button

                AlertDialog al = (AlertDialog) dialog;

                //ir buscar as ediitexts

                EditText etArtista = (EditText) al.findViewById(R.id.editText_artista);
                EditText etAlbum = (EditText) al.findViewById(R.id.editText_album);
                EditText etAno = (EditText) al.findViewById(R.id.editText_ano);

                //ir buscar as strings das edittexts

                String artista = etArtista.getText().toString();
                String Album = etAlbum.getText().toString();
                String ano = etAno.getText().toString();

                //criar um novo album

                String album = artista + " | " + Album + " | " + ano;


                //adicionar o album a lista de albums
                albums.add(album);

                //mostrar a lista de contactos actualizada


                Toast.makeText(MainActivity.this, "Carregou no Botão OK", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                Toast.makeText(MainActivity.this, "Carregou no Botão Cancel", Toast.LENGTH_SHORT).show();
            }
        });
// Set other dialog properties
        builder.setTitle("Insira um novo Album");

        builder.setMessage("Insira o Artista, Album, Ano de Lançamento e Rating para adicionar");

// Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
