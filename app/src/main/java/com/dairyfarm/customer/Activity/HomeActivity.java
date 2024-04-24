package com.dairyfarm.customer.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.MenuItem;
import android.widget.Toast;

import com.dairyfarm.customer.Fragments.BottomNavigationMenuFourFragment;
import com.dairyfarm.customer.Fragments.BottomNavigationMenuOneFragment;
import com.dairyfarm.customer.Fragments.BottomNavigationMenuThreeFragment;
import com.dairyfarm.customer.Fragments.BottomNavigationMenuTwoFragment;
import com.dairyfarm.customer.R;
import com.dairyfarm.customer.Utils.AtClass;
import com.dairyfarm.customer.Utils.BottomNavigationMenuSelectedItemIDManager;
import com.dairyfarm.customer.Utils.BottomNavigationViewHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.dairyfarm.customer.Utils.BottomNavigationStatusConstants.MENU_ITEM_FOUR_INT;
import static com.dairyfarm.customer.Utils.BottomNavigationStatusConstants.MENU_ITEM_FOUR_STRING;
import static com.dairyfarm.customer.Utils.BottomNavigationStatusConstants.MENU_ITEM_ONE_INT;
import static com.dairyfarm.customer.Utils.BottomNavigationStatusConstants.MENU_ITEM_ONE_STRING;
import static com.dairyfarm.customer.Utils.BottomNavigationStatusConstants.MENU_ITEM_THREE_INT;
import static com.dairyfarm.customer.Utils.BottomNavigationStatusConstants.MENU_ITEM_THREE_STRING;
import static com.dairyfarm.customer.Utils.BottomNavigationStatusConstants.MENU_ITEM_TWO_INT;
import static com.dairyfarm.customer.Utils.BottomNavigationStatusConstants.MENU_ITEM_TWO_STRING;

public class HomeActivity extends AppCompatActivity {
    String statusOfFragment;
    Fragment fragment;
    BottomNavigationView navigation;
    Dialog ForceUpdateDialog;
    boolean canExit = false;

    public static boolean isDestroy = false;
    public static boolean isStop = false;
    public static boolean isPause = false;
    public static boolean isResume = false;

    AtClass atClass;
    BottomNavigationMenuSelectedItemIDManager bottomNavigationMenuSelectedItemIDManager;

    int theme = R.style.AppTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(this.theme);
        setContentView(R.layout.activity_home);
        atClass = new AtClass();
        bottomNavigationMenuSelectedItemIDManager = new BottomNavigationMenuSelectedItemIDManager(this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        isDestroy = false;
        isStop = false;
        isPause = false;
        isResume = false;
    }


    @Override
    protected void onResume() {
        isResume = true;
        super.onResume();
        setUpBottomNavigation();
    }

    private void setUpBottomNavigation() {
        canExit = false;

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(navigation);

        if (atClass.CheckEmptyString(this, bottomNavigationMenuSelectedItemIDManager.getBottomNavigationMenuSelectedItemID(), "")) {
            if (bottomNavigationMenuSelectedItemIDManager.getBottomNavigationMenuSelectedItemID().equals(MENU_ITEM_ONE_STRING)) {
                selectedposition(MENU_ITEM_ONE_INT);
            } else if (bottomNavigationMenuSelectedItemIDManager.getBottomNavigationMenuSelectedItemID().equals(MENU_ITEM_TWO_STRING)) {
                selectedposition(MENU_ITEM_TWO_INT);
            } else if (bottomNavigationMenuSelectedItemIDManager.getBottomNavigationMenuSelectedItemID().equals(MENU_ITEM_THREE_STRING)) {
                selectedposition(MENU_ITEM_THREE_INT);
            } else {
                selectedposition(MENU_ITEM_ONE_INT);
            }
        } else {
            selectedposition(MENU_ITEM_ONE_INT);
        }
    }


    private void selectedposition(int position) {
        switch (position) {
            case MENU_ITEM_ONE_INT:
                fragment = new BottomNavigationMenuOneFragment();
                loadFragment(fragment);
                bottomNavigationMenuSelectedItemIDManager.setBottomNavigationMenuSelectedItemID(MENU_ITEM_ONE_STRING);
                navigation.setSelectedItemId(R.id.navigation_one);
                break;


            case MENU_ITEM_TWO_INT:
                fragment = new BottomNavigationMenuTwoFragment();
                loadFragment(fragment);
                bottomNavigationMenuSelectedItemIDManager.setBottomNavigationMenuSelectedItemID(MENU_ITEM_TWO_STRING);
                navigation.setSelectedItemId(R.id.navigation_two);
                break;

            case MENU_ITEM_THREE_INT:
                fragment = new BottomNavigationMenuThreeFragment();
                loadFragment(fragment);
                bottomNavigationMenuSelectedItemIDManager.setBottomNavigationMenuSelectedItemID(MENU_ITEM_THREE_STRING);
                navigation.setSelectedItemId(R.id.navigation_three);
                break;

            case MENU_ITEM_FOUR_INT:
                fragment = new BottomNavigationMenuFourFragment();
                loadFragment(fragment);
                bottomNavigationMenuSelectedItemIDManager.setBottomNavigationMenuSelectedItemID(MENU_ITEM_FOUR_STRING);
                navigation.setSelectedItemId(R.id.navigation_four);
                break;
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        //transaction.addToBackStack(null);
        transaction.disallowAddToBackStack();
        transaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_one:
                    fragment = new BottomNavigationMenuOneFragment();
                    loadFragment(fragment);
                    bottomNavigationMenuSelectedItemIDManager.setBottomNavigationMenuSelectedItemID(MENU_ITEM_ONE_STRING);
                    changeStatusBarColor(false);
                    return true;

                case R.id.navigation_two:
                    fragment = new BottomNavigationMenuTwoFragment();
                    loadFragment(fragment);
                    bottomNavigationMenuSelectedItemIDManager.setBottomNavigationMenuSelectedItemID(MENU_ITEM_TWO_STRING);
                    changeStatusBarColor(true);
                    return true;

                case R.id.navigation_three:
                    fragment = new BottomNavigationMenuThreeFragment();
                    loadFragment(fragment);
                    bottomNavigationMenuSelectedItemIDManager.setBottomNavigationMenuSelectedItemID(MENU_ITEM_THREE_STRING);
                    return true;

                case R.id.navigation_four:
                    fragment = new BottomNavigationMenuFourFragment();
                    loadFragment(fragment);
                    bottomNavigationMenuSelectedItemIDManager.setBottomNavigationMenuSelectedItemID(MENU_ITEM_FOUR_STRING);
                    return true;

            }
            return false;
        }
    };

    private void changeStatusBarColor(boolean isWhite) {
        if (isWhite) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorWhite, this.getTheme()));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorWhite));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary, this.getTheme()));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (canExit) {
            super.onBackPressed();
        } else {
            Toast.makeText(HomeActivity.this, getString(R.string.home_press_back_again_to_exit_text), Toast.LENGTH_SHORT).show();
            canExit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    canExit = false;
                }
            }, 3 * 1000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isStop = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    public void changeTheme(int newTheme) {
        this.theme = newTheme;
        recreate();
    }

}
