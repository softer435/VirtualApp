package com.lody.virtual.client.service;

import java.io.Serializable;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.service.am.StubInfo;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;

/**
 * @author Lody
 *
 */
public class ProviderCaller {

	public static Bundle call(StubInfo stubInfo, String methodName, String arg, Bundle bundle) {
		return call(stubInfo.providerInfo.authority, VirtualCore.getCore().getContext(), methodName, arg, bundle);
	}

	public static Bundle call(String authority, Context context, String methodName, String arg, Bundle bundle) {
		Uri uri = Uri.parse("content://" + authority);
		ContentResolver contentResolver = context.getContentResolver();
		return contentResolver.call(uri, methodName, arg, bundle);
	}

	public static final class Builder {

		private Context context;

		private Bundle bundle = new Bundle();

		private String methodName;
		private String auth;
		private String arg;

		public Builder(Context context, String auth) {
			this.context = context;
			this.auth = auth;
		}

		public Builder methodName(String name) {
			this.methodName = name;
			return this;
		}

		public Builder arg(String arg) {
			this.arg = arg;
			return this;
		}

		public Builder addArg(String key, Object value) {
			if (value != null) {
				if (value instanceof IBinder) {
					if (Build.VERSION.SDK_INT >= 18) {
						bundle.putBinder(key, (IBinder) value);
					} else {
						// noinspection deprecation
						bundle.putIBinder(key, (IBinder) value);
					}
				} else if (value instanceof Boolean) {
					bundle.putBoolean(key, (Boolean) value);
				} else if (value instanceof Integer) {
					bundle.putInt(key, (Integer) value);
				} else if (value instanceof String) {
					bundle.putString(key, (String) value);
				} else if (value instanceof Serializable) {
					bundle.putSerializable(key, (Serializable) value);
				} else if (value instanceof Bundle) {
					bundle.putBundle(key, (Bundle) value);
				} else if (value instanceof Parcelable) {
					bundle.putParcelable(key, (Parcelable) value);
				} else {
					throw new IllegalArgumentException("Unknown type " + value.getClass() + " in Bundle.");
				}
			}
			return this;
		}

		public Bundle call() {
			return ProviderCaller.call(auth, context, methodName, arg, bundle);
		}

	}

}
