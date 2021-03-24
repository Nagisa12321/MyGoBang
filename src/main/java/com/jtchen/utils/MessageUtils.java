package com.jtchen.utils;

import com.alibaba.fastjson.JSONObject;
import com.jtchen.online.client.GoBangClient;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.*;

/**
 * @author jtchen
 * @version 1.0
 * @date 2021/3/24 11:18
 */
public class MessageUtils {

	private static final Logger logger  = Logger.getLogger(GoBangClient.class);

	public static void openDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "POINT", JOptionPane.INFORMATION_MESSAGE);
	}


}
