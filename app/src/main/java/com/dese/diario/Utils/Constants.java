package com.dese.diario.Utils;

import android.os.Environment;

import com.dese.diario.POJOS.VariablesLogin;

/**
 * Created by Eduardo on 11/08/2017.
 */

public class Constants {
    static VariablesLogin variablesLogin= new VariablesLogin();

    public static final String mMainDirectory = "/Mi Diario";
    public static final String mDirectoryPictures = "/Mi Diario/Perfil/";
    public static final String mDirectoryProfile = "/Mi Diario/Perfil/"+"Profile"+variablesLogin.getIdusuario().toString()+".jpg";
    public static final String mDirectoryHolder = "/Mi Diario/Perfil/"+"Holder"+variablesLogin.getIdusuario().toString()+".jpg";
    public static final String mDownloadDirectory = "/Mi Diario/Descargas/";
    public static final String mAudioDirectory = "/Mi Diario/Audio/";
    public static final String mImageDirectory = "/Mi Diario/Image/";
    public static final String mDocumentsDirectory = Environment.getExternalStorageDirectory()+ "/Mi Diario/Documentos/";
}
