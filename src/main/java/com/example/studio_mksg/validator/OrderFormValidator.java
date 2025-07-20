package com.example.studio_mksg.validator;

import com.example.studio_mksg.controller.form.OrderForm;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@Getter
@Setter
public class OrderFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return OrderForm.class.isAssignableFrom(clazz);
    }

    private boolean isBlankOrWidthSpace(String value) {
        return StringUtils.isBlank(value) || value.replaceAll("　", "").trim().isEmpty();
    }

    @Override
    public void validate(Object target, Errors errors) {
        OrderForm form = (OrderForm) target;

        if (StringUtils.isBlank(form.getLastName()) ||
                form.getLastName().replaceAll("　", "").trim().isEmpty()) {
            errors.rejectValue("lastName", "required.lastName", "・氏を入力してください");
        }
        if (isBlankOrWidthSpace(form.getFirstName())) {
            errors.rejectValue("firstName", "required.firstName", "・名を入力してください");
        }

        if (isBlankOrWidthSpace(form.getPostcode())) {
            errors.rejectValue("postcode", "required.postcode", "・郵便番号を入力してください");
        }

        if (isBlankOrWidthSpace(form.getAddress())) {
            errors.rejectValue("address", "required.address", "・住所を入力してください");
        }

        if (isBlankOrWidthSpace(form.getTel())) {
            errors.rejectValue("tel", "required.tel", "・電話番号を入力してください");
        }

        // メールと確認メール
        if (isBlankOrWidthSpace(form.getEmail())) {
            errors.rejectValue("email", "required.email", "・メールアドレスを入力してください");
        }

        if (isBlankOrWidthSpace(form.getConfirmationEmail())) {
            errors.rejectValue("confirmationEmail", "required.confirmationEmail", "・確認用メールアドレスを入力してください");
        } else if (!form.getEmail().equals(form.getConfirmationEmail())) {
            errors.rejectValue("confirmationEmail", "notMatch.confirmationEmail", "・メールアドレスと確認用メールアドレスが一致しません");
        }

        // 支払い情報
        if (isBlankOrWidthSpace(form.getPaymentMethod())) {
            errors.rejectValue("paymentMethod", "required.paymentMethod", "・支払方法を選択してください");
        }

        // クレジット支払の場合のみチェック
        if ("1".equals(form.getPaymentMethod())) {
            if (isBlankOrWidthSpace(form.getCreditNumber())) {
                errors.rejectValue("creditNumber", "required.creditNumber", "・クレジットカード番号を入力してください");
            }
            if (isBlankOrWidthSpace(form.getSecurityCode())) {
                errors.rejectValue("securityCode", "required.securityCode", "・セキュリティコードを入力してください");
            }
            if (isBlankOrWidthSpace(form.getExpiryMonth())) {
                errors.rejectValue("expiryMonth", "required.expiryMonth", "・有効期限（月）を選択してください");
            }
            if (isBlankOrWidthSpace(form.getExpiryYear())) {
                errors.rejectValue("expiryYear", "required.expiryYear", "・有効期限（年）を選択してください");
            }
            if (isBlankOrWidthSpace(form.getNumberOfPayment())) {
                errors.rejectValue("numberOfPayment", "required.numberOfPayment", "・支払回数を選択してください");
            }
        }

        // お届け先情報（differentReceiverがtrueの場合）
        if (Boolean.TRUE.equals(form.getDifferentReceiver())) {
            if (isBlankOrWidthSpace(form.getReceiverLastName())) {
                errors.rejectValue("receiverLastName", "required.receiverLastName", "・お届け先の氏を入力してください");
            }
            if (isBlankOrWidthSpace(form.getReceiverFirstName())) {
                errors.rejectValue("receiverFirstName", "required.receiverFirstName", "・お届け先の名を入力してください");
            }
            if (isBlankOrWidthSpace(form.getReceiverPostcode())) {
                errors.rejectValue("receiverPostcode", "required.receiverPostcode", "・お届け先の郵便番号を入力してください");
            }
            if (isBlankOrWidthSpace(form.getReceiverAddress())) {
                errors.rejectValue("receiverAddress", "required.receiverAddress", "・お届け先の住所を入力してください");
            }
            if (isBlankOrWidthSpace(form.getReceiverTel())) {
                errors.rejectValue("receiverTel", "required.receiverTel", "・お届け先の電話番号を入力してください");
            }
        }

        if (isBlankOrWidthSpace(form.getGiftHope())) {
            errors.rejectValue("giftHope", "required.giftHope", "・ギフト希望有無を選択してください");
        }

        // ギフトメッセージ（希望ありの場合のみ）
        if (Boolean.TRUE.equals(form.getGiftMassageFlag())) {
            if (isBlankOrWidthSpace(form.getGiftMassage())) {
                errors.rejectValue("giftMassage", "required.giftMassage", "・ギフトメッセージを入力してください");
            }
        }
    }
}
