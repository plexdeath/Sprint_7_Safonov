public class OrderFilters {
    private int limit;
    private int page;
    private String nearestStation;

    public OrderFilters(int limit, int page, String nearestStation) {
        this.limit = limit;
        this.page = page;
        this.nearestStation = nearestStation;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getNearestStation() {
        return nearestStation;
    }

    public void setNearestStation(String nearestStation) {
        this.nearestStation = nearestStation;
    }


}
