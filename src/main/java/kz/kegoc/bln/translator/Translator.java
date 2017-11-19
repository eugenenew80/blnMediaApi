package kz.kegoc.bln.translator;

import kz.kegoc.bln.entity.common.HasLang;
import kz.kegoc.bln.entity.media.Lang;

public interface Translator<T extends HasLang> {
    T translate(T entity, Lang defLang);
}
