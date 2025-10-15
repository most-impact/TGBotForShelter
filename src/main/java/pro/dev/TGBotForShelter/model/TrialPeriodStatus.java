package pro.dev.TGBotForShelter.model;


/**
 * Статус испытательного срока
 */
public enum TrialPeriodStatus {
    /** Испытательный срок активен */
    ACTIVE,
    /** Испытательный срок пройден */
    PASSED,
    /** Испытательный срок продлен на 14 дней */
    EXTENDED_14,
    /** Испытательный срок продлен на 30 дней */
    EXTENDED_30,
    /** Испытательный срок не пройден */
    FAILED
}