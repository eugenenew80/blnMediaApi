package kz.kegoc.bln.repository;

import kz.kegoc.bln.common.repository.Repository;
import kz.kegoc.bln.imp.raw.AtTimeValueRaw;
import javax.ejb.Local;
import java.util.List;

@Local
public interface AtTimeValueRawRepository extends Repository<AtTimeValueRaw> {
    void saveAll(List<AtTimeValueRaw> list);
}
