package com.example.marcusedition.professionalshopper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by victor on 1.10.15.
 */
public class MainActivity extends Activity {

    /**
     * Метод для створення початкової імітації завантаження і перехід до іншого activity
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_page);

        Thread logoTimer = new Thread()
        {
            public void run()
            {
                try
                {
                    int logoTimer = 0;
                    while(logoTimer < 2000)
                    {
                        sleep(100);
                        logoTimer = logoTimer + 100;
                    }
                    startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    finish();
                }
            }
        };
        logoTimer.start();
    }
}