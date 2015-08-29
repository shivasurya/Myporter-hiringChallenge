package app.porter.com.datastore;

import java.io.Serializable;

/**
 * Created by S.Shivasurya on 8/29/2015.
 */
public class parcel implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;
    String name;
    String image;
    String date;
    String type;
    String weight;
    String phone;
    String price;
    String color;
    String link;
    Double latitude;
    Double longitude;
    int quantity;
    public parcel(String name,
            String image,
            String date,
            String type,
            String weight,
            String phone,
            String price,
            int quantity,
            String color,
            String link,
            Double latitude,
            Double longitude){
        this.name = name;
        this.image= image;
        this.date= date;
        this.type= type;
        this.quantity = quantity;
        this.weight= weight;
        this.phone= phone;
        this.price= price;
        this.color= color;
        this.link= link;
        this.latitude= latitude;
        this.longitude= longitude;
    }
    public String getLink(){ return  this.link; }
    public int getQuantity(){return this.quantity;}
    public String getName()
    {
        return this.name;
    }
    public String getImage()
    {
        return this.image;
    }
    public String getPrice()
    {
        return  this.price;
    }
    public String getType()
    {
        return this.type;
    }
    public Double getLatitude(){return this.latitude;}
    public Double getLongitude(){return this.longitude;}
    public String getWeight(){return this.weight;}
    public String getPhone(){return this.phone;}
    public String getDate(){ return this.date; }
}
