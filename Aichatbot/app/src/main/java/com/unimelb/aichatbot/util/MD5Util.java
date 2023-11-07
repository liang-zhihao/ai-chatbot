package com.unimelb.aichatbot.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static String md5Avatar(String email) {
        StringBuilder stringBuilder = new StringBuilder();
        String simpleUUID = DigestUtils.md5Hex(email);
        stringBuilder.append("https://www.gravatar.com/avatar/")
                .append(simpleUUID + ".png")
                .append("?f=y&")
                .append("d=wavatar&")
                // .append("d=retro&") GitHub-style avatar
                // Optional d=""
                // 404: If no image is associated with the email hash, no image is loaded and an HTTP 404 (File Not Found) response is returned.
                //   mp: (Mystery Person) A simple cartoon-style outline of a person (does not change with the email hash).
                // identicon: Geometric patterns based on the email hash.
                // monsterid: Generated "monsters" with different colors, faces, etc.
                // wavatar: Generated faces with different features and backgrounds.
                //   retro: Awesome 8-bit arcade-style pixelated faces.
                // robohash: Generated robots with different colors, facial features, etc.
                .append("r=g");
        return stringBuilder.toString();
    }
}
