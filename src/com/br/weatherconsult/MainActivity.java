package com.br.weatherconsult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

import com.br.weatherconsult.ReadXMLFile.MetarInfo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private TextView metarText;
	private TextView observationTimeText;
	private TextView tempText;
	private TextView dewpointText;
	private TextView windDirSpeedText;
	private TextView visibilityText;
	private TextView altimHgText;
	private TextView flightCategoryText;
	private TextView elevationText;
	private TextView skyConditionText;
	private RequestWeatherTask asyncTaskWeather;
	private ProgressDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super .onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		metarText = (TextView) findViewById(R.id.resultMetar);
		observationTimeText = (TextView) findViewById(R.id.observationTime);
		tempText = (TextView) findViewById(R.id.tempC);
		dewpointText = (TextView) findViewById(R.id.dewpointC);
		windDirSpeedText = (TextView) findViewById(R.id.windDirSpeed);
		visibilityText = (TextView) findViewById(R.id.visibility);
		altimHgText = (TextView) findViewById(R.id.altimHg);
		flightCategoryText = (TextView) findViewById(R.id.flightCategory);
		elevationText = (TextView) findViewById(R.id.elevationM);
		skyConditionText = (TextView) findViewById(R.id.skyCondition);
		Button btnBuscar = (Button) findViewById(R.id.btnBuscar);
		
		btnBuscar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				exibirMetar();
			}
		});		
	}

	private void exibirMetar() {
		try {
			EditText txtIcao = (EditText) findViewById(R.id.textoICAO);
			if (txtIcao.getText() != null && txtIcao.getText().toString().length() == 4)	{
				String serviceLink = "http://aviationweather.gov/adds/dataserver_current/httpparam?dataSource=metars&requestType=retrieve&format=xml&stationString=" 
						+ txtIcao.getText().toString() + "&hoursBeforeNow=1.5";

				 dialog = ProgressDialog.show(MainActivity.this, getString(R.string.search), getString(R.string.progressOn));
				asyncTaskWeather = new RequestWeatherTask();
				asyncTaskWeather.execute(serviceLink);
			}
			else	{
				Toast.makeText(this, "Código ICAO inválido.", Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Erro ao solicitar web-service." + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onDestroy() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null ;
		}

		if (asyncTaskWeather != null)	{
			asyncTaskWeather.cancel(true);	
		}
		
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed()
	{
		finish();
	}
	
	/*
	 * Classe assíncrona que consulta o web-service de meteorologia e aplica o resultado na tela. 
	 */
	public class RequestWeatherTask extends AsyncTask<String, Void, MetarInfo>   {

		@Override
		protected MetarInfo doInBackground(String... params) {
			HttpURLConnection con = null ;
			URL url = null ;
			MetarInfo response = null ;
			try {
				url = new URL(params[0]);
				con = (HttpURLConnection) url.openConnection();
				
				ReadXMLFile readXML = new ReadXMLFile(con.getInputStream());
				response = readXML.getMetarInfo();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				con.disconnect();
			}
			return response;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.show();
		}
		
		@Override
		protected void onPostExecute(MetarInfo result) {
			super.onPostExecute(result);
			metarText.setText(result.getCurrentMetar());
			observationTimeText.setText(getString(R.string.observationTime) + " " + result.getObservationTime());
			tempText.setText(getString(R.string.tempC) + " " + result.getTempC());
			dewpointText.setText(getString(R.string.dewpointC) + " " + result.getDewpointC());
			windDirSpeedText.setText(getString(R.string.wind) + " " + result.getWindDirDegrees() + " / " + result.getWindSpeedKt());
			visibilityText.setText(getString(R.string.visibility) + " " + result.getVisibilityStatuteMi());
			altimHgText.setText(getString(R.string.altimHg) + " " + result.getAltimHg());
			flightCategoryText.setText(getString(R.string.flightCategory) + " " + result.getFlightCategory());
			elevationText.setText(getString(R.string.elevationM) + " " + result.getElevationM());
			skyConditionText.setText(result.getSkyCondition());
			dialog.dismiss();
		}

	}

}
