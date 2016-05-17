package com.amsspecialist;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amsspecialist.classes.GlobalUtilities;
import com.amsspecialist.classes.RoundedTransformation;
import com.amsspecialist.fragments.Forums_Fragment;
import com.amsspecialist.fragments.News_Fragment;
import com.amsspecialist.fragments.Notifications_Fragment;
import com.astuetz.PagerSlidingTabStrip;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.drawer)
    DrawerLayout drawer;
    @InjectView(R.id.drawnmenu)
    RecyclerView Listmenu;
   @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.titlebar)
    TextView titleBar;
    @InjectView(R.id.titlebar2)
    TextView titleBar2;
    @InjectView(R.id.pager)
    ViewPager pager;
    @InjectView(R.id.tabs)
    PagerSlidingTabStrip tabs;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<Object> items;
    private MenuDrawerAdapter mAdapter;
    private BroadcastReceiver mHandleMessageReceiver;
    Stack<Integer> pageHistory;
    private SharedPreferences settings;
    private Typeface font;
    private Boolean Login;
    Notifications_Fragment notifyFrm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);


       if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(null);
            font = Typeface.createFromAsset(getAssets(), "neuroplitical.ttf");

            titleBar.setTypeface(font);
            titleBar2.setTypeface(font);
            mDrawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.drawer_open,
                    R.string.drawer_close) {
                /**
                 * Called when a drawer has settled in a completely closed state.
                 */
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);

                }

                /**
                 * Called when a drawer has settled in a completely open state.
                 */
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);

                }
            };


            items = new ArrayList<Object>();

            items.add(new ItemProfile("No Login", "http://www.amsspecialist.com/images/unknown.jpg"));
            items.add(new ItemCategory("Sitios"));
            items.add(new ItemNormal(R.drawable.ic_globe, getString(R.string.item_visitforum)));
            items.add(new ItemNormal(R.drawable.ic_facebook, getString(R.string.item_facebook)));
            items.add(new ItemNormal(R.drawable.ic_video, getString(R.string.item_video)));
            items.add(new ItemCategory("Autoplay media Studio"));
            items.add(new ItemNormal(R.drawable.ic_help, getString(R.string.item_help)));
            items.add(new ItemNormal(R.drawable.ic_file, "AMS Files"));
            items.add(new ItemCategory("Aplicación"));
            items.add(new ItemNormal(R.drawable.ic_settings, getString(R.string.action_settings)));
            mAdapter = new MenuDrawerAdapter(items);
            drawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
            drawer.setDrawerListener(mDrawerToggle);
            Listmenu.setHasFixedSize(true);
            Listmenu.setLayoutManager(new LinearLayoutManager(this));
            Listmenu.setItemAnimator(new DefaultItemAnimator());

            Listmenu.setAdapter(mAdapter);
        }

        PagerAdapter adapter = new PagerAdapter(
                getSupportFragmentManager());
        adapter.addFragment(Forums_Fragment.newInstance());
        adapter.addFragment(News_Fragment.newInstance());
        adapter.addFragment(Notifications_Fragment.newInstance());

        pageHistory = new Stack<Integer>();
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(3);
        tabs.setViewPager(pager);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Toast.makeText(getApplicationContext(),String.valueOf(position),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPageSelected(int position) {

                pageHistory.push(position);
                //Toast.makeText(getApplicationContext(),String.valueOf(position),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //Toast.makeText(getApplicationContext(),String.valueOf(state),Toast.LENGTH_LONG).show();
            }
        });
        mHandleMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle bun = intent.getExtras();
                String newpost = bun.getString("newpost");
                String newmps = bun.getString("newMps");
                int nmsg = bun.getInt("mensage");

                if (nmsg != 0) {

                    Toast.makeText(context, GlobalUtilities._mensages[nmsg],
                            Toast.LENGTH_LONG).show();
                }

                ((PagerAdapter) pager.getAdapter()).setnotificationnumber(Integer.parseInt(newpost));
                ((Notifications_Fragment) getSupportFragmentManager().findFragmentByTag(getFragmentTag(2))).updateInfo();
                //Toast.makeText(getApplicationContext(),newpost,Toast.LENGTH_LONG).show();

            }
        };
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                GlobalUtilities.DISPLAY_MESSAGE_ACTION));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(getApplicationContext(),GlobalUtilities.getPrefs(getApplicationContext()).getString("user","No Login"),Toast.LENGTH_LONG).show();
        if (!GlobalUtilities.TestConnection(getApplicationContext())) {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("No tienes conexion a internet esta aplicación requiere de ella")
                    .setContentText("Deseas cerrar el programa?")
                    .setConfirmText("Yes")
                    .setCancelText("Cancel")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            MainActivity.this.finish();
                        }
                    })
                    .show();
        }
        mAdapter.mDataset.set(
                0,
                new ItemProfile(GlobalUtilities.getPrefs(getApplicationContext()).getString("user",
                        getString(R.string.no_user_login)) + " ", GlobalUtilities.getPrefs(getApplicationContext()).getString(
                        "avatar",
                        "http://www.amsspecialist.com/images/unknown.jpg")));

        mAdapter.notifyDataSetChanged();

        Login = GlobalUtilities.getPrefs(getApplicationContext()).getBoolean("login", false);
        if (Login) {
            GlobalUtilities.checkRegisterGCM(getApplicationContext());
            ContentValues cn = GlobalUtilities
                    .checkNewdata(getApplicationContext());
            int newmps = Integer.parseInt(cn.get(GlobalUtilities.NEWMPS).toString());
            int newpost = Integer.parseInt(cn.get(GlobalUtilities.NEWPOST).toString());
            GlobalUtilities.editSharePrefs(getApplication())
                    .putInt("newpost", newpost)
                    .putInt("newmps", newmps)
                    .commit();
            ((PagerAdapter) pager.getAdapter())
                    .setnotificationnumber(newpost + newmps);

        } else {
            GlobalUtilities.editSharePrefs(getApplication())
                    .putString("user", "No Login")
                    .putInt("newpost", 0)
                    .putInt("newmps", 0).commit();

        }
        if(getIntent().getBooleanExtra("notification",false)){
            pager.setCurrentItem(2);
            getIntent().removeExtra("notification");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        drawer.closeDrawers();
    }

    @Override
    public void onBackPressed() {

        if (pager.getCurrentItem() == 0) {

            new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                    .setTitleText(getString(R.string.exit))
                    .setContentText(getString(R.string.exit_question))
                    .setConfirmText(getString(R.string.ok))
                    .setCancelText(getString(R.string.Cancel))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            MainActivity.this.finish();
                        }
                    })
                    .show();


        } else {
            if (pager.getCurrentItem() == 2 && getSupportFragmentManager().findFragmentByTag("newpost") != null) {
                //NewPOST_Fragment frm = (NewPOST_Fragment) getSupportFragmentManager().findFragmentByTag("newpost");
                getSupportFragmentManager()
                        .beginTransaction()
                        .remove(getSupportFragmentManager()
                                .findFragmentByTag("newpost"))
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            } else {
                if (pageHistory.size() > 1) {
                    pageHistory.pop();
                }

                pager.setCurrentItem(pageHistory.peek().intValue());

            }
        }
    }


    //Adapters...
    public class PagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {getString(R.string.tab_forums), getString(R.string.tab_news), getString(R.string.tab_notifications), getString(R.string.tab_settings)};
        private List<Fragment> fragments;

        public PagerAdapter(FragmentManager fm) {

            super(fm);
            this.fragments = new ArrayList<Fragment>();
        }

        public void setnotificationnumber(Integer n) {
            this.TITLES[2] = String.format(getString(R.string.tab_notifications) + " (%d)", n);

            this.notifyDataSetChanged();
        }

        public void addFragment(Fragment fragment) {
            this.fragments.add(fragment);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return TITLES[position];
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }
    }

    public class ItemProfile {
        String imageUserUrl;
        String User;

        public ItemProfile(String user, String imageUserUrl) {
            this.setImageUserUrl(imageUserUrl);
            this.setUser(user);
        }

        public String getImageUserUrl() {
            return imageUserUrl;
        }

        public void setImageUserUrl(String imageUserUrl) {
            this.imageUserUrl = imageUserUrl;
        }

        public String getUser() {
            return User;
        }

        public void setUser(String user) {
            User = user;
        }
    }

    public class ItemNormal {
        String text;
        int drawableicon;

        public ItemNormal(int icon, String text) {
            this.setDrawableicon(icon);
            this.setTextItem(text);
        }

        public String getTextItem() {
            return text;
        }

        public void setTextItem(String text) {
            this.text = text;
        }

        public int getDrawableicon() {
            return drawableicon;
        }

        public void setDrawableicon(int drawableicon) {
            this.drawableicon = drawableicon;
        }
    }

    public class ItemCategory {
        String textCategory;

        public ItemCategory(String text) {
            this.textCategory = text;
        }

    }

//adpater menuDrawer

    public class MenuDrawerAdapter extends RecyclerView.Adapter<MenuDrawerAdapter.ViewHolder> {

        private List<Object> mDataset;


        // Adapter's Constructor
        public MenuDrawerAdapter(List<Object> myDataset) {
            mDataset = myDataset;

        }

        @Override
        public int getItemViewType(int position) {
            int type = 0;
            if (mDataset.get(position) instanceof ItemProfile) {
                type = 0;
            } else if (mDataset.get(position) instanceof ItemNormal) {
                type = 1;
            } else if (mDataset.get(position) instanceof ItemCategory) {
                type = 2;
            }
            return type;
        }

        // Create new views. This is invoked by the layout manager.
        @Override
        public MenuDrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder holder = null;
            View v = null;
            switch (viewType) {
                case 0:
                    v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_profile, parent, false);
                    break;
                case 1:
                    v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_drawner_normal, parent, false);
                    break;
                case 2:
                    v = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_category, parent, false);
                    break;
                default:
                    break;
            }


            holder = new ViewHolder(v, viewType);
            return holder;
        }

        // Replace the contents of a view. This is invoked by the layout manager.
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            switch (getItemViewType(position)) {
                case 0:
                    Picasso.with(getApplicationContext())
                            .load(((ItemProfile) mDataset.get(position)).getImageUserUrl())
                            .transform(new RoundedTransformation(80, 4))
                            .into(holder.ImageUser);
                    holder.textuser.setText(((ItemProfile) mDataset.get(position)).getUser());
                    break;
                case 1:
                    holder.IconItem.setImageResource(((ItemNormal) mDataset.get(position)).getDrawableicon());
                    holder.textItem.setText(((ItemNormal) mDataset.get(position)).getTextItem());
                    break;
                case 2:
                    holder.textCategory.setText(((ItemCategory) mDataset.get(position)).textCategory);
                    break;
                default:
                    break;
            }


        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        // Implement OnClick listener. The clicked item text is displayed in a Toast message.


        // Create the ViewHolder class to keep references to your views
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView ImageUser;
            TextView textuser;
            ImageView IconItem;
            TextView textItem;
            TextView textCategory;

            public ViewHolder(View v, int type) {
                super(v);
                switch (type) {
                    case 0:
                        ImageUser = ButterKnife.findById(v, R.id.imageUser);
                        textuser = ButterKnife.findById(v, R.id.textUser);
                        break;
                    case 1:
                        IconItem = ButterKnife.findById(v, R.id.icondrawner);
                        textItem = ButterKnife.findById(v, R.id.textitemdrawner);
                        break;
                    case 2:
                        textCategory = ButterKnife.findById(v, R.id.textCategory);
                        break;
                    default:
                        break;
                }

                v.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                switch (ViewHolder.this.getPosition()) {
                    case 0:
                        if (Login) {
                            String username = GlobalUtilities.getPrefs(getApplicationContext()).getString("user", "");

                            new SweetAlertDialog(view.getContext(), SweetAlertDialog.NORMAL_TYPE)
                                    .setTitleText(username)
                                    .setContentText(getString(R.string.close_session) + username + "?")
                                    .setConfirmText(getString(R.string.ok))
                                    .setCancelText(getString(R.string.Cancel))
                                    .showCancelButton(true)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            GlobalUtilities.unlogin(getApplicationContext());
                                            onResume();

                                            PagerAdapter adapter = (PagerAdapter) pager.getAdapter();
                                            ((Notifications_Fragment) getSupportFragmentManager().findFragmentByTag(getFragmentTag(2))).updateInfo();

                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        } else {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                        break;
                    case 1:
                       /* startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.amsspecialist.com")));*/
                        break;
                    case 2:
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.amsspecialist.com")));
                        break;
                    case 3:
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://www.facebook.com/AMSSpecialist")));
                        break;
                    case 4:
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://www.youtube.com/channel/UCCNG1OowkXGvbi-6T1J6zBA")));
                        break;
                    case 6:
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://amsspecialist.com/help.php")));
                        break;
                    case 7:
                        startActivity(new Intent(getApplicationContext(), AMSFilesActivity.class));
                        break;
                    case 9:
                        startActivity(new Intent(getApplicationContext(), Settings_Activity.class));
                        break;
                }

            }
        }
    }

    @Override
    protected void onStop() {

        super.onStop();
        if (mHandleMessageReceiver != null) {
            unregisterReceiver(mHandleMessageReceiver);
        }

    }

    private String getFragmentTag(int pos) {
        return "android:switcher:" + R.id.pager + ":" + pos;
    }

}
