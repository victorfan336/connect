package com.victor.remotedebug;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author victor
 * @Description
 * @date 2018/9/18
 */
public class MainActivity extends AppCompatActivity {

    Button button_connect;
    TextView tv_ip;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_connect = (Button) findViewById(R.id.connect);
        tv_ip = (TextView) findViewById(R.id.ip);
        button_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新UI操作；
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int netType = NetUtils.getNetWorkConnectionType(getApplicationContext());
                            switch (netType) {
                                case ConnectivityManager.TYPE_WIFI:
                                    Toast.makeText(MainActivity.this, "wifi连接", Toast.LENGTH_SHORT).show();
                                    tv_ip.setText(NetUtils.getWifiIP(getApplicationContext()) + ":5555");
                                    break;
                                case ConnectivityManager.TYPE_MOBILE:
                                    tv_ip.setText(NetUtils.getMobileIP(getApplicationContext()) + ":5555");
                                    Toast.makeText(MainActivity.this, "移动数据连接", Toast.LENGTH_SHORT).show();
                                    break;
                                case ConnectivityManager.TYPE_ETHERNET:
                                    tv_ip.setText(NetUtils.getMobileIP(getApplicationContext()) + ":5555");
                                    Toast.makeText(MainActivity.this, "以太网连接", Toast.LENGTH_SHORT).show();
                                    break;
                                    default:
                                        tv_ip.setText("没有网络");
                                        break;
                            }

                            //开启adbd端口
                            ShellUtils.execCommand("setprop service.adb.tcp.port 5555", true);
                            ShellUtils.execCommand("stop adbd", true);
                            ShellUtils.execCommand("start adbd", true);
                        } catch (StringIndexOutOfBoundsException e) {
                            Toast.makeText(MainActivity.this, "wifi未连接", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }


}
