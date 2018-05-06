package com.aroraaman.myshopify.model;

public class Province {

    public final String provinceCode;
    public final String province;

    public Province(String provinceCode, String province) {
        this.provinceCode = provinceCode;
        this.province = province;
    }

    @Override
    public int hashCode() {
        int result = provinceCode.hashCode();
        result = 31 * result + province.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Province)) {
            return false;
        }

        Province province1 = (Province) o;
        return provinceCode.equals(province1.provinceCode) && province.equals(province1.province);
    }

}
