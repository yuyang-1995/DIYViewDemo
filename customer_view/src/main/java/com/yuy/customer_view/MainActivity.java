package com.yuy.customer_view;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View view = findViewById(R.id.id_progress);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //使用属性动画
                Animator animator =  ObjectAnimator.ofInt(view, "progress", 0, 100).setDuration(3000);
                animator.start();
            }
        });


    }
}
