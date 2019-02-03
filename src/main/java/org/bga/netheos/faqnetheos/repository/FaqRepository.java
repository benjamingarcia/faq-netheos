package org.bga.netheos.faqnetheos.repository;

import lombok.Data;
import org.bga.netheos.faqnetheos.dto.Faq;

import javax.annotation.ManagedBean;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@Data
public class FaqRepository{

    private List<Faq> faqs;

    public FaqRepository() {
        this.faqs = new ArrayList<>(0);
    }

    public boolean add(Faq faq){
        return faqs.add(faq);
    }
}
