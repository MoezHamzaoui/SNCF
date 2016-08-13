package com.ov.exercice.sncf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/*
 * Consigne:
 * Enregistrez vous sur https://data.sncf.com/api (http://www.navitia.io/register/) pour récupérer une clé d'API.
 * Ce code va servir à récupérer les horaires de trains au départ de montparnasse.
 * Puis ensuite les afficher sous forme Heure : destination
 * Vous utiliserez votre clé dans l'url d'appel de l'API.
 * Il ne respecte pas les standards et doit être nettoyé puis refactoré pour
 * être réutilisable et compréhensible.
 */

public class GetTrajets {

	public static  HttpURLConnection sHttpURLConnection;




	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		try {
			sHttpURLConnection = (HttpURLConnection) (new URL("https://api.sncf.com/v1/coverage/sncf/stop_areas/stop_area:OCE:SA:87391003/departures?datetime=20160729T150423")).openConnection();
			sHttpURLConnection.setRequestProperty("Authorization", "Basic " + (Base64.getUrlEncoder().encodeToString("c83416c4-5f06-49a6-b207-608dc98862de:".getBytes())));
			sHttpURLConnection.setRequestMethod("GET");
			sHttpURLConnection.setRequestProperty("Content-length", "0");
			sHttpURLConnection.setUseCaches(false);
			sHttpURLConnection.setAllowUserInteraction(false);
			sHttpURLConnection.connect();
			int lResponsableCode = sHttpURLConnection.getResponseCode();
			String json = "";
			switch (lResponsableCode) {
			case 200:
			case 201:
				BufferedReader oDepartMontparnasse = new BufferedReader(new InputStreamReader(sHttpURLConnection.getInputStream()));
				StringBuilder lDepartToMontparnasse = new StringBuilder();
				String line;
				while ((line = oDepartMontparnasse.readLine()) != null) {
					lDepartToMontparnasse.append(line+"\n");
				}
				oDepartMontparnasse.close();
				json = lDepartToMontparnasse.toString();
				JSONArray mDepartMonparnasseList = (JSONArray) (((JSONObject)(new JSONParser()).parse(json)).get("departures"));
				System.out.println("Prochains départs de Montparnasse :");
				for (int lDepartMontparnasseIndex=0 ; lDepartMontparnasseIndex<mDepartMonparnasseList.size()-1 ; lDepartMontparnasseIndex++) {
					System.out.println(((JSONObject)((JSONObject)mDepartMonparnasseList.get(lDepartMontparnasseIndex)).get("stop_date_time")).get("departure_date_time")+" : "+((JSONObject)((JSONObject)((JSONObject)mDepartMonparnasseList.get(lDepartMontparnasseIndex)).get("route")).get("line")).get("name"));



				}
			}
		} catch (MalformedURLException iException) {
			// TODO Auto-generated catch block
			System.err.println(iException);
		} catch (IOException iException) {
			// TODO Auto-generated catch block
			System.err.println(iException);
		} catch (ParseException iException) {
			// TODO Auto-generated catch block
			System.err.println(iException);
		}
	}
}
