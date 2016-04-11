package com.newly_dawn.app.wsn;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

public class Index {
    public void build(AppCompatActivity context){

        final View nextView;
        Log.i("zl_debug", "Index");
        Toolbar toolbar = (Toolbar) context.findViewById(R.id.toolbar);
        context.setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) context.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Index Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        listener(context);
    }
    public void listener(AppCompatActivity context){

    }
}
