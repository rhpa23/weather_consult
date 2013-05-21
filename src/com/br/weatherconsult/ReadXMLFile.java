package com.br.weatherconsult;

import java.io.InputStream;

import javax.xml.parsers.*;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;

import android.os.Environment;

public class ReadXMLFile {

	private MetarInfo metarInfo;
	/** @return the metarInfo	 */
	public MetarInfo getMetarInfo() {
		return metarInfo;
	}

	public ReadXMLFile(InputStream file)	{
		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				boolean bLastObservation = true;
				boolean bRawMetarText = false;
				boolean bObservationTime = false;
				boolean bTempC = false;
				boolean bDewpointC = false;
				boolean bWindDirDegrees = false;
				boolean bWindSpeedKt = false;
				boolean bVisibilityMi = false;
				boolean bAltimHg = false;
				boolean bFlightCategory = false;
				boolean bElevationM = false;

				public void startElement(String uri, String localName,String qName, 
						Attributes attributes) throws SAXException {

					if (bLastObservation)	{

						if (qName.equalsIgnoreCase("raw_text")) {
							bRawMetarText = true;
						}
						if (qName.equalsIgnoreCase("observation_time")) {
							bObservationTime = true;
						}
						if (qName.equalsIgnoreCase("temp_c")) {
							bTempC = true;
						}
						if (qName.equalsIgnoreCase("dewpoint_c")) {
							bDewpointC = true;
						}
						if (qName.equalsIgnoreCase("wind_dir_degrees")) {
							bWindDirDegrees = true;
						}
						if (qName.equalsIgnoreCase("wind_speed_kt")) {
							bWindSpeedKt = true;
						}
						if (qName.equalsIgnoreCase("visibility_statute_mi")) {
							bVisibilityMi = true;
						}
						if (qName.equalsIgnoreCase("altim_in_hg")) {
							bAltimHg = true;
						}
						if (qName.equalsIgnoreCase("flight_category")) {
							bFlightCategory = true;
						}
						if (qName.equalsIgnoreCase("elevation_m")) {
							bElevationM = true;
						}
						if (qName.equalsIgnoreCase("sky_condition")) {
							if (attributes.getValue(0) != null)	{
								metarInfo.addSkyCondition(attributes.getValue(0));
								if (attributes.getValue(1) != null)
									metarInfo.addSkyCondition(": " + attributes.getValue(1) + "m");
								
								metarInfo.addSkyCondition("\n");// Quebra de linha.
							}
						}
					}
					
				}

				public void endElement(String uri, String localName,
						String qName) throws SAXException {

					// quando fechar a primeira tag 'Metar' encerra a leitura para ficar com a 1ª observação
					if (qName.equalsIgnoreCase("METAR")) {
						bLastObservation = false;
					}
					
				}

				public void characters(char ch[], int start, int length) throws SAXException {
					if (metarInfo == null)	{
						metarInfo = new MetarInfo();
					}
					
					if (bRawMetarText) {
						metarInfo.setCurrentMetar(new String(ch, start, length));
						bRawMetarText = false;
					}
					
					if (bObservationTime) {
						metarInfo.setObservationTime(new String(ch, start, length));
						bObservationTime = false;
					}
					if (bTempC) {
						metarInfo.setTempC(new String(ch, start, length) + "º");
						bTempC = false;
					}
					if (bDewpointC) {
						metarInfo.setDewpointC(new String(ch, start, length) + "º");
						bDewpointC = false;
					}
					if (bWindDirDegrees) {
						metarInfo.setWindDirDegrees(new String(ch, start, length) + "º");
						bWindDirDegrees = false;
					}
					if (bWindSpeedKt) {
						metarInfo.setWindSpeedKt(new String(ch, start, length) + "kt");
						bWindSpeedKt = false;
					}
					if (bVisibilityMi) {
						metarInfo.setVisibilityStatuteMi(new String(ch, start, length) + "SM");
						bVisibilityMi = false;
					}
					if (bAltimHg) {
						metarInfo.setAltimHg(new String(ch, start, length) + "hg");
						bAltimHg = false;
					}
					if (bFlightCategory) {
						metarInfo.setFlightCategory(new String(ch, start, length));
						bFlightCategory = false;
					}
					if (bElevationM) {
						metarInfo.setElevationM(new String(ch, start, length) + "m");
						bElevationM = false;
					}
				}

			};

			saxParser.parse(file, handler);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	


	public class MetarInfo	{
		private String currentMetar;
		private String observationTime;
		private String tempC;
		private String dewpointC;
		private String windDirDegrees;
		private String windSpeedKt;
		private String visibilityStatuteMi;
		private String altimHg;
		private String flightCategory;
		private String elevationM;
		private String skyCondition = new String();
		
		
		public String getCurrentMetar() {
			return currentMetar;
		}

		public void setCurrentMetar(String currentMetar) {
			this.currentMetar = currentMetar;
		}
		
		/**
		 * @return the observationTime
		 */
		public String getObservationTime() {
			return observationTime;
		}

		/**
		 * @param observationTime the observationTime to set
		 */
		public void setObservationTime(String observationTime) {
			this.observationTime = observationTime;
		}
		
		/**
		 * @return the tempC
		 */
		public String getTempC() {
			return tempC;
		}

		/**
		 * @param tempC the tempC to set
		 */
		public void setTempC(String tempC) {
			this.tempC = tempC;
		}

		/**
		 * @return the dewpointC
		 */
		public String getDewpointC() {
			return dewpointC;
		}

		/**
		 * @param dewpointC the dewpointC to set
		 */
		public void setDewpointC(String dewpointC) {
			this.dewpointC = dewpointC;
		}

		/**
		 * @return the windDirDegrees
		 */
		public String getWindDirDegrees() {
			return windDirDegrees;
		}

		/**
		 * @param windDirDegrees the windDirDegrees to set
		 */
		public void setWindDirDegrees(String windDirDegrees) {
			this.windDirDegrees = windDirDegrees;
		}

		/**
		 * @return the windSpeedKt
		 */
		public String getWindSpeedKt() {
			return windSpeedKt;
		}

		/**
		 * @param windSpeedKt the windSpeedKt to set
		 */
		public void setWindSpeedKt(String windSpeedKt) {
			this.windSpeedKt = windSpeedKt;
		}

		/**
		 * @return the visibilityStatuteMi
		 */
		public String getVisibilityStatuteMi() {
			return visibilityStatuteMi;
		}

		/**
		 * @param visibilityStatuteMi the visibilityStatuteMi to set
		 */
		public void setVisibilityStatuteMi(String visibilityStatuteMi) {
			this.visibilityStatuteMi = visibilityStatuteMi;
		}

		/**
		 * @return the altimHg
		 */
		public String getAltimHg() {
			return altimHg;
		}

		/**
		 * @param altimHg the altimHg to set
		 */
		public void setAltimHg(String altimHg) {
			this.altimHg = altimHg;
		}

		/**
		 * @return the flightCategory
		 */
		public String getFlightCategory() {
			return flightCategory;
		}

		/**
		 * @param flightCategory the flightCategory to set
		 */
		public void setFlightCategory(String flightCategory) {
			this.flightCategory = flightCategory;
		}

		/**
		 * @return the elevationM
		 */
		public String getElevationM() {
			return elevationM;
		}

		/**
		 * @param elevationM the elevationM to set
		 */
		public void setElevationM(String elevationM) {
			this.elevationM = elevationM;
		}

		/**
		 * @return the skyCondition
		 */
		public String getSkyCondition() {
			return skyCondition;
		}

		/**
		 * @param skyCondition the skyCondition to set
		 */
		public void setSkyCondition(String skyCondition) {
			this.skyCondition = skyCondition;
		}

		/**
		 * @param skyCondition the skyCondition to set
		 */
		public void addSkyCondition(String skyCondition) {
			this.skyCondition += skyCondition;
		}
	}

	
}
