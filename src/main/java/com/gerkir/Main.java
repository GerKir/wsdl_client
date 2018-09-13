package com.gerkir;

import unisoft.ws.FNSNDSCAWS2;
import unisoft.ws.FNSNDSCAWS2Port;
import unisoft.ws.fnsndscaws2.request.NdsRequest2;
import unisoft.ws.fnsndscaws2.response.NdsResponse2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        FNSNDSCAWS2 fns = new FNSNDSCAWS2();
        FNSNDSCAWS2Port fnsPort = fns.getFNSNDSCAWS2Port();

        NdsRequest2.NP np = generateNp(args);
        if (np == null) return;

        NdsRequest2 request = new NdsRequest2();
        request.getNP().add(np);

        //Отображение response
        NdsResponse2.NP respNp = fnsPort.ndsRequest2(request).getNP().get(0);
        printNp(respNp);
    }

    private static NdsRequest2.NP generateNp(String[] args) {
        NdsRequest2.NP np = new NdsRequest2.NP();
        if (args.length == 2) {
            np.setINN(args[0]);
            np.setKPP(args[1]);
            return np;
        } else if (args.length == 1) {
            np.setINN(args[0]);
            return np;
        } else {
            System.out.println("Введите аргументы:");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                System.out.println("Введите ИНН:");
                np.setINN(br.readLine().trim());
                System.out.println("Введите КПП или оставьте поле пустым");
                String kpp = br.readLine().trim();
                np.setKPP(kpp.isEmpty() ? null : kpp);
                return np;
            } catch (IOException e) {
                System.out.println("Данные введены некорректно ");
                e.printStackTrace();
            }
            return np;
        }
    }

    private static void printNp(NdsResponse2.NP respNp) {
        if (respNp.getKPP() == null) {
            System.out.println("Данные о Индивидуальном предпринимателе: " +
                    "ИНН: " + respNp.getINN() + " " +
                    "Статус: " + getStateMessageIp(respNp.getState()));
        } else {
            System.out.println("Данные о Юридическом лице: " +
                    "ИНН: " + respNp.getINN() + " " +
                    "КПП: " + respNp.getKPP() + " " +
                    "Статус: " + getStateMessageUl(respNp.getState()));
        }
    }

    /**
     * @return Возвращает строку статуса для ИП
     */
    private static String getStateMessageIp(String state) {
        return StateMessages.StateMessagesForIp[Integer.parseInt(state)];
    }

    /**
     * @return Возвращает строку статуса для Юридического лица
     */
    private static String getStateMessageUl(String state) {
        return StateMessages.StateMessagesForUl[Integer.parseInt(state)];
    }

}
