package com.art4muslim.zedalmouhajer.adapters;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.art4muslim.zedalmouhajer.BaseApplication;
import com.art4muslim.zedalmouhajer.R;
import com.art4muslim.zedalmouhajer.fragments.SelectedAssociationFragment;
import com.art4muslim.zedalmouhajer.menu.Beneficiar;
import com.art4muslim.zedalmouhajer.models.Request;
import com.art4muslim.zedalmouhajer.session.Constants;
import com.art4muslim.zedalmouhajer.session.SessionManager;
import com.art4muslim.zedalmouhajer.utils.AlertDialogManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Beneficiar> beneficiars;
	BaseApplication application;

	String from;
	FragmentTransaction fragmentTransaction;
	public CustomListAdapter(Activity activity, List<Beneficiar> myOrderModelItems, String from, FragmentTransaction fragmentTransaction) {
		this.activity = activity;
		this.beneficiars = myOrderModelItems;
		this.from=from;
		this.fragmentTransaction=fragmentTransaction;
		application = (BaseApplication)activity.getApplicationContext();

	}

	@Override
	public int getCount() {
		return beneficiars.size();
	}

	@Override
	public Object getItem(int location) {
		return beneficiars.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.list_row, null);

		final Beneficiar beneficiar = beneficiars.get(position);
		ImageView imgUser = (ImageView)  convertView.findViewById(R.id.imgUser);
		TextView txt_name = (TextView) convertView.findViewById(R.id.txt_name);
		Button btnDelete = (Button) convertView.findViewById(R.id.btnDelete);
		Button btnModify = (Button) convertView.findViewById(R.id.btnModify);
		Button btnActivate = (Button) convertView.findViewById(R.id.btnActivate);

		txt_name.setText(beneficiar.getName());

		if (from.equals("BEN")){
			btnActivate.setVisibility(View.GONE);
			btnDelete.setVisibility(View.GONE);
			btnModify.setVisibility(View.GONE);
		}

		btnDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String url = Constants.DELETE_BEN_REQUEST;
				updateDelete(beneficiar.getId(), application.getAssociation().getId_user(),url);

			}
		});
		btnModify.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			}
		});
		btnActivate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String url = Constants.ACTIVE_BEN_REQUEST;
				updateDelete(beneficiar.getId(), application.getAssociation().getId_user(),url);
			}
		});

		return convertView;
	}
	public void clear() {
		// TODO Auto-generated method stub
		beneficiars.clear();

	}

	private void updateDelete(final String idBen, final String idAss, String url ) {

		//+"phone="+phone+"&password="+password;

		Log.e("List ben", "updateDelete url "+url);
		Log.e("List ben", "idBen  "+idBen);
		Log.e("List ben", "idAss "+idAss);


		StringRequest LoginFirstRequest = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				Log.v("registerRequest","response == " +response);

				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(response);
					boolean _status = jsonObject.getBoolean("status");
					String msg = jsonObject.getString("message");

					if (_status) {



					} else {


						AlertDialogManager.showAlertDialog(activity ,activity.getResources().getString(R.string.app_name),msg,false,0);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}


			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {


				if (error instanceof AuthFailureError) {

					AlertDialogManager.showAlertDialog(activity,activity.getResources().getString(R.string.app_name),activity.getResources().getString(R.string.authontiation),false,3);

				} else if (error instanceof ServerError) {
					AlertDialogManager.showAlertDialog(activity,activity.getResources().getString(R.string.app_name),activity.getResources().getString(R.string.servererror),false,3);
				} else if (error instanceof NetworkError) {
					AlertDialogManager.showAlertDialog(activity,activity.getResources().getString(R.string.networkerror),activity.getResources().getString(R.string.networkerror),false,3);

				} else if (error instanceof ParseError) {
				} else if (error instanceof NoConnectionError) {
				} else if (error instanceof TimeoutError) {
					AlertDialogManager.showAlertDialog(activity,activity.getResources().getString(R.string.app_name),activity.getResources().getString(R.string.timeouterror),false,3);
				}
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<>();
				params.put("bene_id", idBen);
				params.put("ass_id", idAss);

				return params;
			}
			@Override
			protected Response<String> parseNetworkResponse(NetworkResponse response) {
				try {

					String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
					return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
				} catch (UnsupportedEncodingException e) {
					return Response.error(new ParseError(e));
				}
			}
		};


		// Adding request to request queue
		BaseApplication.getInstance().addToRequestQueue(LoginFirstRequest);


	}


}