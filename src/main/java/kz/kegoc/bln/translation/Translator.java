package kz.kegoc.bln.translation;

import kz.kegoc.bln.entity.common.HasLang;
import kz.kegoc.bln.entity.media.raw.Lang;

public interface Translator<T extends HasLang> {
    T translate(T entity, Lang defLang);
}
