package com.dese.diario.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dese.diario.DetailPublication;
import com.dese.diario.Item.ItemClickListener;
import com.dese.diario.Item.MyHolderP;
import com.dese.diario.Objects.Experence;
import com.dese.diario.POJOS.VariablesLogin;
import com.dese.diario.R;
import com.dese.diario.Utils.Urls;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Eduardo on 04/04/2017.
 */

public class Adapter_Experence extends RecyclerView.Adapter<MyHolderP> {
    ArrayList<Experence> listapublicaciones;
    Context context;
    View.OnLongClickListener longClickListener;
    String dataFoto, dataNombre, dataToken;

    public Adapter_Experence(ArrayList<Experence> listapublicaciones, Context context) {


        this.listapublicaciones = listapublicaciones;
        this.context = context;

    }

    @Override
    public MyHolderP onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_publication, parent, false);



        return new MyHolderP(view);
    }

    @Override
    public void onBindViewHolder(MyHolderP holder, int position) {
        final String _ide= listapublicaciones.get(position).getIdpublicacion();
            final String parabuscar=listapublicaciones.get(position).getParabuscaruser();
        getDataFriends(parabuscar);

        final String d = listapublicaciones.get(position).getFecha();
        final String p = listapublicaciones.get(position).getObservaciones();
        final String u = dataNombre; //listapublicaciones.get(position).getNombre();
        final String t = listapublicaciones.get(position).getTitulo();
        final String f = dataFoto; //listapublicaciones.get(position).getFoto();
        final String pa = listapublicaciones.get(position).getIdpublicacion();
        final String sen=listapublicaciones.get(position).getSentimiento();
        final String eva=listapublicaciones.get(position).getEvaluacion();
        final String ana=listapublicaciones.get(position).getAnalisis();
        final String con= listapublicaciones.get(position).getConclusion();
        final String plan=listapublicaciones.get(position).getPlanaccion();


        holder.tvFechaRec.setText(d);
        holder.tvPublicationRec.setText(p);
        holder.tvUserRec.setText(u);
        holder.titlePublication.setText(t);
       // holder.tvSentimientosRec.setText(sen);
        if(!sen.isEmpty()){
            holder.tlMainSelect.setVisibility(View.VISIBLE);
            holder.cvFeels.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(context)
                            .title("¿Que esta pensando y sintiendo?")
                            .content(sen)
                            .positiveText("Listo")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();

                }
            });
        }else{
            holder.cvFeels.setVisibility(View.GONE);
            holder.tlMainSelect.setVisibility(View.GONE);
        }
        if(!eva.isEmpty()){
            holder.cvTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(context)
                            .title("¿Que es lo bueno y malo de esta experiencia?")
                            .content(eva)
                            .positiveText("Listo")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });
        }else{
            holder.cvTest.setVisibility(View.GONE);

        }
        if(!ana.isEmpty()){

            holder.cvAnalyze.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(context)
                            .title("¿Que sentido puede tener esta experiencia?")
                            .content(ana)
                            .positiveText("Listo")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });
        }else{
         holder.cvAnalyze.setVisibility(View.GONE);
        }
        if(!con.isEmpty()){
            holder.cvConclusion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(context)
                            .title("¿Qué mas podria haber hecho?")
                            .content(con)
                            .positiveText("Listo")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });
        }else{
            holder.cvConclusion.setVisibility(View.GONE);
        }
        if(!plan.isEmpty()){
            holder.cvPlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(context)
                            .title("¿Qué haría en una experiencia similar?")
                            .content(plan)
                            .positiveText("Listo")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });
        }else {
            holder.cvPlan.setVisibility(View.GONE);
        }




        Picasso.with(context)
                .load(Urls.download + f)
                .resize(250, 250)
                .centerCrop()
                .into(holder.imProfileRec);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                openDetailActivity(_ide,t, u, d, p, f, pa, sen, eva, ana, con, plan);

            }
        });


    }


    @Override
    public int getItemCount() {
        return (null != listapublicaciones ? listapublicaciones.size() : 0);
        //return listapublicaciones.size();
    }


    private void openDetailActivity(String _ide, String t, String u, String d, String p, String f ,
                                    final String pa, String sen, String tes, String ana, String con,
                                    String plan ) {
        Intent i = new Intent(context, DetailPublication.class);
        ListRepublication rp= new ListRepublication();
        //PACK DATA TO SEND
        i.putExtra("_IDE_KEY", _ide);
        i.putExtra("TITLE_KEY", t);
        i.putExtra("USER_KEY", u);
        i.putExtra("DATA_KEY", d);
        i.putExtra("PUBLI_KEY", p);
        i.putExtra("FOTO_KEY", f);
        i.putExtra("PAPA", pa);
        i.putExtra("SEN_KEY", sen);
        i.putExtra("TES_KEY", tes);
        i.putExtra("ANA_KEY", ana);
        i.putExtra("CON_KEY", con);
        i.putExtra("PLAN_KEY", plan);
        //open activity
        context.startActivity(i);

    }

    private void getDataFriends(final String parabuscaruser) {

        final List finalUser = null;
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.filtrousuarioXid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //JSONArray jsonArray = null;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                            try {

                                JSONArray jsonarray = new JSONArray(response);
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                                    //  System.out.println(jsonobject);
                                    VariablesLogin varllogin = new VariablesLogin();

                                    dataNombre = jsonobject.getString("nombre");
                                   dataFoto= jsonobject.getString("foto");
                                   dataToken= jsonobject.getString("token");

                                    /// System.out.println(listpublicaciones);

                                }
                            } catch (JSONException e) {
                                Log.e("UUUps!!!!!", "Error!!" + e);
                            }
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("°_°", "ocurio un error !");
            }

        }

        ) {
            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("idusuario", parabuscaruser);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };


        queue.add(stringRequest);

    }

}




