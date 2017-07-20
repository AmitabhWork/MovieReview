package com.example.sadanandk.moviereviews;

/**
 * Created by sadanandk on 7/12/2017.
 */

public class PojoFavourite
{
    String m_id,title,r_date,overview,poster,trailer,rating;

    public PojoFavourite(String m_id, String title, String r_date, String overview, String poster, String trailer, String rating) {
        this.m_id = m_id;
        this.title = title;
        this.r_date = r_date;
        this.overview = overview;
        this.poster = poster;
        this.trailer = trailer;
        this.rating = rating;
    }

    public String getM_id() {
        return m_id;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getR_date() {
        return r_date;
    }

    public void setR_date(String r_date) {
        this.r_date = r_date;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
