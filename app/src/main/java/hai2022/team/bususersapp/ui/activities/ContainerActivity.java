package hai2022.team.bususersapp.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import hai2022.team.bususersapp.R;
import hai2022.team.bususersapp.databinding.ActivityContainerBinding;
import hai2022.team.bususersapp.models.Bus;
import hai2022.team.bususersapp.ui.fragments.AddBusFragment;
import hai2022.team.bususersapp.ui.fragments.BusDetailsFragment;
import hai2022.team.bususersapp.ui.fragments.DriverDetailsFragment;
import hai2022.team.bususersapp.utils.Constants;

public class ContainerActivity extends AppCompatActivity {
    ActivityContainerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContainerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setSupportActionBar(binding.maintoolbar.maintoolbar);
        Intent i = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        if (i.getStringExtra(Constants.FRAGMENT).equals(Constants.ADD_BUS_FRAGMENT)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.ContainerActivity_container, AddBusFragment.newInstance()).commit();
            binding.maintoolbar.maintoolbarTvTitle.setText("Add Bus");
        } else if (i.getStringExtra(Constants.FRAGMENT).equals(Constants.DRIVER_DETAILS_FRAGMENT)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.ContainerActivity_container, DriverDetailsFragment.newInstance((Bus) getIntent().getSerializableExtra("Bus"), (Uri) getIntent().getParcelableExtra("Uri"))).commit();
            binding.maintoolbar.maintoolbarTvTitle.setText("Bus Now");
        } else if (i.getStringExtra(Constants.FRAGMENT).equals(Constants.BUS_DETAILS_FRAGMENT)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.ContainerActivity_container, BusDetailsFragment.newInstance((Bus) getIntent().getSerializableExtra("Bus"))).commit();
            binding.maintoolbar.maintoolbarTvTitle.setText("Bus Now");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}