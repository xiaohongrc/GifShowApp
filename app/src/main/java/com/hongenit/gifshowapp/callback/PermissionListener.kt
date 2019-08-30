package com.hongenit.gifshowapp.callback

interface PermissionListener {

    fun onGranted()

    fun onDenied(deniedPermissions: List<String>)

}
