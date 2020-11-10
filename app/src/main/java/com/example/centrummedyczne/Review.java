package com.example.centrummedyczne;

public class Review {
    Float rate;
    String review;

    public Review(){}

    public Review(Float rate, String review) {
        this.rate = rate;
        this.review = review;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
