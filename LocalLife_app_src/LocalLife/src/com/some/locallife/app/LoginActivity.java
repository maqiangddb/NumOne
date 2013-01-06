package com.some.locallife.app;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.apache.http.auth.UsernamePasswordCredentials;

import com.some.locallife.R;
import com.some.locallife.data.LocalLife;
import com.some.locallife.data.error.LocalException;
import com.some.locallife.data.type.LocalType;
import com.some.locallife.preferences.MyPreferences;
import com.some.locallife.util.TaskManager.DoTask;
import com.some.locallife.util.TaskManager.SetData;
import com.some.locallife.util.Util;
import com.some.locallife.util.TaskManager.BaseDataTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private static final int DLG_BASE = 0;
	private static final int DLG_REGISTER = DLG_BASE + 1;

	private BaseDataTask mLoginTask;
	private BaseDataTask mRegisterTask;
	private String user;
	private String password;
	private EditText mUsernameEditText;
	private EditText mPasswordEditText;

	private View textEntryView;


	private LocalLife mApi;
	private LocalLifeApplication mApp;



	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.login);

		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		TextView tv = (TextView) this.findViewById(R.id.titleName);
		tv.setText(R.string.login_activity_title);

		this.mApp = (LocalLifeApplication)this.getApplication();
		this.mApi = this.mApp.getLocalLife();
		this.initUi();

	}

	@Override
	public Dialog onCreateDialog(int id, Bundle bundle) {
		switch(id) {
		case DLG_REGISTER:
			LayoutInflater inflater = LayoutInflater.from(this);
			LoginActivity.this.textEntryView = inflater.inflate(R.layout.register_dlg, null);

			return new AlertDialog.Builder(this)
			.setTitle(R.string.dlg_title_register)
			.setView(textEntryView)
			.setPositiveButton(R.string.register_dlg_posi_btn_text,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

							LoginActivity.this.doRegister();
						}
					})
			.setNegativeButton(R.string.register_dlg_nega_btn_text,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					})
			.create();
		}

		return null;
	}

	private Dialog createDialog(String title, String message, int Ndlgtext, int Pdlgtext,
			DialogInterface.OnClickListener NdlgListener, DialogInterface.OnClickListener PdlgListener) {
		return new AlertDialog.Builder(this)
			.setTitle(title)
			.setMessage(message)
			.setNegativeButton(Ndlgtext, NdlgListener)
			.setPositiveButton(Pdlgtext, PdlgListener)
			.create();
	}

	private ProgressDialog mProgressDialog;

	private ProgressDialog showProgressDialog() {
		if (this.mProgressDialog == null) {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setTitle("");
			dialog.setMessage("");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			this.mProgressDialog = dialog;
		}
		this.mProgressDialog.show();
		return this.mProgressDialog;
	}

	private void dismissProgressDialog() {
		this.mProgressDialog.dismiss();
	}

	private void doLogin() {
		Util.login("doLogin==");
		this.showProgressDialog();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = prefs.edit();
		String user = "test";//this.mUsernameEditText.getText().toString();
		String password = "password";//this.mPasswordEditText.getText().toString();
		MyPreferences.loginUser(mApi, user, password, editor);
		this.mLoginTask.execute();

	}

	private void doRegister() {
		this.user = ((EditText)this.textEntryView.findViewById(R.id.edit_username_rig)).getText().toString();
		this.password = ((EditText)this.textEntryView.findViewById(R.id.edit_password_rig)).getText().toString();
		this.mRegisterTask.execute();

	}

	private void getResponse(LocalType data) {
		this.dismissProgressDialog();
		if (data != null) {
			Toast.makeText(this, R.string.login_success, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, R.string.login_fail, Toast.LENGTH_LONG).show();
		}
		this.finish();
	}

	private void showUserPasswordErrorDlg() {

	}

	private void initUi() {

		this.mLoginTask = this.mApp.getTaskManager().new BaseDataTask();
		this.mLoginTask = this.mApp.getTaskManager().createTask(this.mLoginTask, new DoTask() {

			@Override
			public LocalType doTask() {
				// TODO Auto-generated method stub

				try {
					Util.login("hasLoginAndPassword===="+LoginActivity.this.mApi.hasLoginAndPassword());
					try {
						return LoginActivity.this.mApi.getFavShopList();
					} catch (LocalException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						switch(e.getErrorCode()) {
						case 400:
							LoginActivity.this.showUserPasswordErrorDlg();
							break;
						}
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;

			}},
			new SetData() {

			@Override
			public void setData(LocalType data) {
				// TODO Auto-generated method stub

				LoginActivity.this.getResponse(data);
			}});

		this.mRegisterTask = this.mApp.getTaskManager().new BaseDataTask();
		this.mRegisterTask = this.mApp.getTaskManager().createTask(mRegisterTask,
				new DoTask() {

					@Override
					public LocalType doTask() {
						// TODO Auto-generated method stub
						LoginActivity.this.showProgressDialog();
						try {
							return LoginActivity.this.mApi.register(LoginActivity.this.user, LoginActivity.this.password);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (LocalException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return null;
					}},
				new SetData() {

					@Override
					public void setData(LocalType data) {
						// TODO Auto-generated method stub
						LoginActivity.this.dismissProgressDialog();

					}});

		final Button loginBtn = (Button) this.findViewById(R.id.login);
		loginBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LoginActivity.this.doLogin();
			}
		});
		final Button regiBtn = (Button) this.findViewById(R.id.register);
		regiBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LoginActivity.this.showDialog(LoginActivity.this.DLG_REGISTER);
			}
		});




		this.mUsernameEditText = (EditText) this.findViewById(R.id.edit_username);
		this.mPasswordEditText = (EditText) this.findViewById(R.id.edit_password);

		TextWatcher fieldVialidatorTextWatcher = new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}};

			this.mUsernameEditText.addTextChangedListener(fieldVialidatorTextWatcher);
			this.mPasswordEditText.addTextChangedListener(fieldVialidatorTextWatcher);

	}

}
