package com.example.bankcards.dto;

import java.util.List;

public class PageCardResponse<Card> {
    private List<Card> cards;
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private int pageSize;

    public PageCardResponse() {
    }

    public PageCardResponse(List<Card> cards, int currentPage, int totalPages, long totalItems, int pageSize) {
        this.cards = cards;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.pageSize = pageSize;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(long totalItems) {
        this.totalItems = totalItems;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
