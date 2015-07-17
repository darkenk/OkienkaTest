package my.darkenk.okienkatest;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

public class OkienkaTest extends Activity {

    ActivityViewWrapper mActivityViewWrapper;
    ViewGroup mDesktop;
    View mResizeFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.okienko);
        mDesktop = (ViewGroup)findViewById(R.id.activity_view);
        mResizeFrame = findViewById(R.id.resize_frame);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.okienka_test, menu);
        Intent filter = new Intent(Intent.ACTION_MAIN, null);
        filter.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(filter, 0);
        for (ResolveInfo resolveInfo : resolveInfoList) {
            ApplicationInfo ai = resolveInfo.activityInfo.applicationInfo;
            MenuItem mi = menu.add(ai.loadLabel(getPackageManager()));
            mi.setIntent(getPackageManager().getLaunchIntentForPackage(ai.packageName));
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mDesktop.addView(new Okienko(OkienkaTest.this, mDesktop, mResizeFrame, item.getIntent()));
        return true;
    }

}
