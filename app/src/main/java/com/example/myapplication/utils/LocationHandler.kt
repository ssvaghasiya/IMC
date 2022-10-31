package com.example.myapplication.utils

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices


class LocationHandler(context: Context) : GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {
    private val context: Context
    private var mGoogleApiClient: GoogleApiClient? = null
    private var connectionListener: ConnectionListener? = null
    private var connectionBundle: Bundle? = null

    init {
        this.context = context
        buildGoogleApiClient()
        connect()
    }

    val googleApiClient: GoogleApiClient?
        get() = mGoogleApiClient

    fun setConnectionListener(connectionListener: ConnectionListener) {
        this.connectionListener = connectionListener
        if (this.connectionListener != null && isConnected) {
            connectionListener.onConnected(connectionBundle)
        }
    }

    fun connect() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient?.connect()
        }
    }

    fun disconnect() {
        if (mGoogleApiClient != null && mGoogleApiClient?.isConnected() == true) {
            mGoogleApiClient?.disconnect()
        }
    }

    val isConnected: Boolean
        get() = mGoogleApiClient != null && mGoogleApiClient?.isConnected() == true

    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(context)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
    }

    override fun onConnected(bundle: Bundle?) {
        connectionBundle = bundle
        if (connectionListener != null) {
            connectionListener!!.onConnected(bundle)
        }
    }

    override fun onConnectionSuspended(i: Int) {
        Log.d(TAG, "onConnectionSuspended: googleApiClient.connect()")
        mGoogleApiClient?.connect()
        if (connectionListener != null) {
            connectionListener!!.onConnectionSuspended(i)
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d(
            TAG,
            "onConnectionFailed: connectionResult = $connectionResult"
        )
        if (connectionListener != null) {
            connectionListener!!.onConnectionFailed(connectionResult)
        }
    }

    interface ConnectionListener {
        fun onConnectionFailed(connectionResult: ConnectionResult)
        fun onConnectionSuspended(i: Int)
        fun onConnected(bundle: Bundle?)
    }

    companion object {
        private val TAG = LocationHandler::class.java.simpleName
    }
}