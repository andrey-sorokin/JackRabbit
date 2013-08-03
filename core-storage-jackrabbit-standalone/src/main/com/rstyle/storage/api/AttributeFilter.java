package main.com.rstyle.storage.api;

import java.util.Map;

/**
 * Коллекция свойств для применения в запросах фильтрации.
 *
 * @author Vladislav.Dremin
 * @author Vadim Solonovich <Vadim.Solonovich@R-Style.com>
 * @version 1.0
 */
public interface AttributeFilter {

    /**
     * Получить список свойств
     *
     * @return список свойств
     */
    public Map<String, Object> getAttributes();

}
