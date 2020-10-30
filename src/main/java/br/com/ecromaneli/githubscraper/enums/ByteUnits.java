package br.com.ecromaneli.githubscraper.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum ByteUnits {
    B, KB, MB, GB, TB;

    public double convertToBytes(double value) {
        double power = (this.ordinal() - 1) * 3;
        return value * Math.pow(10d, power);
    }
}
