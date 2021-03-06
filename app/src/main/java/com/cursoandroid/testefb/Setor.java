package com.cursoandroid.testefb;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;

public class Setor implements Parcelable{
    private String uid;
    private String nome;

    public Setor() {
    }

    protected Setor(Parcel in) {
        uid = in.readString();
        nome = in.readString();
    }

    public static final Creator<Setor> CREATOR = new Creator<Setor>() {
        @Override
        public Setor createFromParcel(Parcel in) {
            return new Setor(in);
        }

        @Override
        public Setor[] newArray(int size) {
            return new Setor[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void salvar(){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebase();
        databaseReference.child("Setores").child(getUid()).setValue(this);//Seta os dados desse próprio objeto usuário
    }

    public void excluir(String id){
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebase();
        databaseReference.child("Setores").child(id).removeValue();
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uid);
        parcel.writeString(nome);
    }
}