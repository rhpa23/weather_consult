package com.br.weatherconsult;

import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.br.weatherconsult.ReadXMLFile.MetarInfo;

public class MainActivity extends SherlockActivity {
	
	private TextView mMetarTxt;
	private TextView mObservationTimeTxt;
	private TextView mTempTxt;
	private TextView mDewpointTxt;
	private TextView mWindDirSpeedTxt;
	private TextView mVisibilityTxt;
	private TextView mAltimHgTxt;
	private TextView mFlightCategoryTxt;
	private TextView mElevationTxt;
	private TextView mSkyConditionTxt;
	private RequestWeatherTask mAsyncTaskWeather;
	private ProgressDialog mDialog;
	private ImageButton mSearchBtn;
	private EditText mIcaoTxt;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super .onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setupViewControls();
	}
	
	/*
	 * Para guardar o estato dos textos mesmo após a mudança de orientação.
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
	  super.onSaveInstanceState(outState);
	  if (mObservationTimeTxt != null)	{
		  outState.putString("ObservationTime", mObservationTimeTxt.getText().toString());
	  }
	  if (mMetarTxt != null)	{
		  outState.putString("Metar", mMetarTxt.getText().toString());
	  }
	  if (mTempTxt != null)	{
		  outState.putString("Temp", mTempTxt.getText().toString());
	  }
	  if (mDewpointTxt != null)	{
		  outState.putString("Dewpoint", mDewpointTxt.getText().toString());
	  }
	  if (mWindDirSpeedTxt != null)	{
		  outState.putString("WindDirSpeed", mWindDirSpeedTxt.getText().toString());
	  }
	  if (mVisibilityTxt != null)	{
		  outState.putString("Visibility", mVisibilityTxt.getText().toString());
	  }
	  if (mAltimHgTxt != null)	{
		  outState.putString("AltimHg", mAltimHgTxt.getText().toString());
	  }
	  if (mFlightCategoryTxt != null)	{
		  outState.putString("FlightCategory", mFlightCategoryTxt.getText().toString());
	  }
	  if (mElevationTxt != null)	{
		  outState.putString("Elevation", mElevationTxt.getText().toString());
	  }
	  if (mSkyConditionTxt != null)	{
		  outState.putString("SkyCondition", mSkyConditionTxt.getText().toString());
	  }
	}
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
	  super.onRestoreInstanceState(savedInstanceState);
	  if (mObservationTimeTxt != null)	{
		  mObservationTimeTxt.setText(savedInstanceState.getString("ObservationTime"));
	  }
	  if (mMetarTxt != null)	{
		  mMetarTxt.setText(savedInstanceState.getString("Metar"));
	  }
	  if (mTempTxt != null)	{
		  mTempTxt.setText(savedInstanceState.getString("Temp"));
	  }
	  if (mDewpointTxt != null)	{
		  mDewpointTxt.setText(savedInstanceState.getString("Dewpoint"));
	  }
	  if (mWindDirSpeedTxt != null)	{
		  mWindDirSpeedTxt.setText(savedInstanceState.getString("WindDirSpeed"));
	  }
	  if (mVisibilityTxt != null)	{
		  mVisibilityTxt.setText(savedInstanceState.getString("Visibility"));
	  }
	  if (mAltimHgTxt != null)	{
		  mAltimHgTxt.setText(savedInstanceState.getString("AltimHg"));
	  }
	  if (mFlightCategoryTxt != null)	{
		  mFlightCategoryTxt.setText(savedInstanceState.getString("FlightCategory"));
	  }
	  if (mElevationTxt != null)	{
		  mElevationTxt.setText(savedInstanceState.getString("Elevation"));
	  }
	  if (mSkyConditionTxt != null)	{
		  mSkyConditionTxt.setText(savedInstanceState.getString("SkyCondition"));
	  }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    
	    setupViewControls(menu); 
	    
	    return super.onCreateOptionsMenu(menu);
	}
	
	/*
	 * Define o Listener de Cliques.
	 */
	private final OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
				case R.id.btnBuscar2:
					exibirMetar();
			        break;
			}
		}
	};
	
	@Override
	protected void onDestroy() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
			mDialog = null ;
		}

		if (mAsyncTaskWeather != null)	{
			mAsyncTaskWeather.cancel(true);	
		}
		
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed()
	{
		finish();
	}
	
	/*
	 * Método específico para inicializar UI .
	 */
	public void setupViewControls() {
		mMetarTxt = (TextView) findViewById(R.id.resultMetar);
		mObservationTimeTxt = (TextView) findViewById(R.id.observationTime);
		mTempTxt = (TextView) findViewById(R.id.tempC);
		mDewpointTxt = (TextView) findViewById(R.id.dewpointC);
		mWindDirSpeedTxt = (TextView) findViewById(R.id.windDirSpeed);
		mVisibilityTxt = (TextView) findViewById(R.id.visibility);
		mAltimHgTxt = (TextView) findViewById(R.id.altimHg);
		mFlightCategoryTxt = (TextView) findViewById(R.id.flightCategory);
		mElevationTxt = (TextView) findViewById(R.id.elevationM);
		mSkyConditionTxt = (TextView) findViewById(R.id.skyCondition);
	}
	
	/*
	 * Método específico para inicializar UI que está no action bar. 
	 */
	private void setupViewControls(Menu menu) {
		mSearchBtn = (ImageButton) menu.findItem(R.id.search_action_bar).getActionView().findViewById(R.id.btnBuscar2);
	    mIcaoTxt = (EditText) menu.findItem(R.id.search_action_bar).getActionView().findViewById(R.id.textoICAO2);
	    
	    mSearchBtn.setOnClickListener(mClickListener);
	}
	
	/*
	 * Exibe as informações do metar a partir do código ICAO utilizado.
	 */
	private void exibirMetar() {
		try {
			if (mIcaoTxt.getText() != null && mIcaoTxt.getText().toString().length() == 4)	{
				String serviceLink = "http://aviationweather.gov/adds/dataserver_current/httpparam?dataSource=metars&requestType=retrieve&format=xml&stationString=" 
						+ mIcaoTxt.getText().toString() + "&hoursBeforeNow=1.5";

				 mDialog = ProgressDialog.show(MainActivity.this, getString(R.string.search), getString(R.string.progressOn));
				mAsyncTaskWeather = new RequestWeatherTask();
				mAsyncTaskWeather.execute(serviceLink);
			}
			else	{
				Toast.makeText(this, "Código ICAO inválido.", Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Erro ao solicitar web-service." + e.getMessage(), Toast.LENGTH_LONG).show();
		}
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
			mDialog.show();
		}
		
		@Override
		protected void onPostExecute(MetarInfo result) {
			super.onPostExecute(result);
			mMetarTxt.setText(result.getCurrentMetar());
			mObservationTimeTxt.setText(getString(R.string.observationTime) + " " + result.getObservationTime());
			mTempTxt.setText(getString(R.string.tempC) + " " + result.getTempC());
			mDewpointTxt.setText(getString(R.string.dewpointC) + " " + result.getDewpointC());
			mWindDirSpeedTxt.setText(getString(R.string.wind) + " " + result.getWindDirDegrees() + " / " + result.getWindSpeedKt());
			mVisibilityTxt.setText(getString(R.string.visibility) + " " + result.getVisibilityStatuteMi());
			mAltimHgTxt.setText(getString(R.string.altimHg) + " " + result.getAltimHg());
			mFlightCategoryTxt.setText(getString(R.string.flightCategory) + " " + result.getFlightCategory());
			mElevationTxt.setText(getString(R.string.elevationM) + " " + result.getElevationM());
			mSkyConditionTxt.setText(result.getSkyCondition());
			mDialog.dismiss();
		}
		
		

	}
}
