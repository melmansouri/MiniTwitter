package com.mel.minitwitter.ui;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mel.minitwitter.R;
import com.mel.minitwitter.common.Constantes;
import com.mel.minitwitter.data.ProfileViewModel;
import com.mel.minitwitter.retrofit.request.RequesUpdateUser;
import com.mel.minitwitter.retrofit.response.User;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private ImageView imageView;
    EditText edtUserName,edtEmail,edtPassword,web,descrip;
    Button btnguardar,btnchangepwd;
    PermissionListener permissionListener;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.profile_fragment, container, false);
        imageView=v.findViewById(R.id.imgProfile);
        edtEmail=v.findViewById(R.id.edtEmail);
        edtPassword=v.findViewById(R.id.edtPwd);
        edtUserName=v.findViewById(R.id.edtUserName);
        web=v.findViewById(R.id.edtWeb);
        descrip=v.findViewById(R.id.edtDesc);
        btnchangepwd=v.findViewById(R.id.btnChangePwd);
        btnguardar=v.findViewById(R.id.btnGuardar);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
            }
        });
        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequesUpdateUser requesUpdateUser=new RequesUpdateUser();
                requesUpdateUser.setEmail(edtEmail.getText().toString());
                requesUpdateUser.setDescripcion(descrip.getText().toString());
                requesUpdateUser.setUsername(edtUserName.getText().toString());
                requesUpdateUser.setWebsite(web.getText().toString());
                requesUpdateUser.setPassword(edtPassword.getText().toString());
                mViewModel.updateProfile(requesUpdateUser);
            }
        });
        btnchangepwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return v;
    }

    private void checkPermissions() {
        PermissionListener dialogOnDeniedListener= DialogOnDeniedPermissionListener.Builder.withContext(getActivity())
                .withTitle("Permisos")
                .withMessage("Permisos necesarios para poder seleccionar una foto")
                .withButtonText("Aceptar")
                .withIcon(R.mipmap.ic_launcher)
                .build();
        permissionListener=new CompositePermissionListener((PermissionListener) getActivity(),dialogOnDeniedListener);

        Dexter.withContext(getActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(permissionListener)
                .check();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        mViewModel.getUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user==null){
                    Toast.makeText(getContext(),"Error actualizando ",Toast.LENGTH_SHORT).show();
                    return;
                }
                Glide.with(getContext())
                        .load("https://www.minitwitter.com/apiv1/uploads/photos/"+user.getPhotoUrl())
                        .placeholder(R.drawable.logo_minitwitter_mini)
                        //La recomiendan para cuando usemos circleimageview
                        .dontAnimate()
                        //Con esto indicamos que no se utilice la cache. Ya que puede dar el caso de que se modifique la imagen pero siga apareciendo la anterior
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imageView);
                edtEmail.setText(user.getEmail());
                edtUserName.setText(user.getUsername());
                web.setText(user.getWebsite());
                descrip.setText(user.getDescripcion());
            }
        });

        mViewModel.getPhotoprofileObserver().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Glide.with(getContext()).load(Constantes.API_MINITWITTER_FILES_URL + s).placeholder(R.drawable.ic_account_circle_azul).dontAnimate()
                        //Con esto indicamos que no se utilice la cache. Ya que puede dar el caso de que se modifique la imagen pero siga apareciendo la anterior
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true).into(imageView);
            }
        });
    }
}