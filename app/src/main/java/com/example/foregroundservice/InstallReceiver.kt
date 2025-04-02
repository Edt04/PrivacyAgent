package com.example.foregroundservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log

class InstallReceiver : BroadcastReceiver() {
    // Called when a broadcast message is received ( in this case, when a new app is installed)
    override fun onReceive(context: Context?, intent: Intent?) {
        // Check if the broadcast action is ACTION_PACKAGE_ADDED, which indicates a new app installation
        if (intent?.action == Intent.ACTION_PACKAGE_ADDED) {
            val packageUri: Uri? = intent.data
            // Get the package name ( app ID)
            val packageName = packageUri?.schemeSpecificPart

            // If the installed package is the same as the current app, exit early.
            if(packageName == context?.packageName) return
            Log.d("InstallReceiver", "New App: $packageName")
            // Get the PackageManager to retrieve app-related info.
            val pm= context?.packageManager
            val permission = pm?.getPackageInfo(packageName!!,PackageManager.GET_PERMISSIONS)

            val requestedPermission = permission?.requestedPermissions
            val grantedFlags = permission?.requestedPermissionsFlags
            // If both permissions and flags are available, iterate through them.
            if(requestedPermission !=null && grantedFlags != null){
                for ( i in requestedPermission.indices){
                    val perm = requestedPermission[i]
                    // Check if the corresponding permission was granted.
                    val granted = (grantedFlags[i] and PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0
                    Log.d("InstallReceiver", "Permission: $perm - Granted: $granted")

                }
            }
        }
    }
}