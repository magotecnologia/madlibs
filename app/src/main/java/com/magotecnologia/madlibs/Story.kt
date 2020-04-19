package com.magotecnologia.madlibs

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Marco-Laptop on 18/04/2020.
 * contiene los datos relacionados a las historias
 */

@Parcelize
data class Story(val name: String, val rawName: String) : Parcelable