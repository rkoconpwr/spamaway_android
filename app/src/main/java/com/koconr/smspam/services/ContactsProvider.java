package com.koconr.smspam.services;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.HashMap;
import java.util.Map;

public class ContactsProvider {

    private static final Map<String, String> contacts = new HashMap();
    private static final String PREFIX = "+48";

    public static void getContactList(Context context) {
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contacts.put(prepareTelNo(phoneNo), name);
                    }
                    pCur.close();
                }
            }
        }
        if (cur != null) {
            cur.close();
        }
    }

    public static String getContactName(String telNumber) {
        if (contacts.containsKey(telNumber)) {
            return contacts.get(telNumber);
        }
        else {
            return telNumber;
        }
    }

    private static String prepareTelNo(String tel) {
        tel = tel.replaceAll("\\s", "");
        if (!tel.startsWith(PREFIX)) {
            tel = PREFIX + tel;
        }
        return tel;
    }
}
