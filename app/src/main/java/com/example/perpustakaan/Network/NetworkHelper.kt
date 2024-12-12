package com.example.kasircafeapp.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class NetworkHelper(private val context: Context) {

    fun isNetworkConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnected
        }
    }
}
