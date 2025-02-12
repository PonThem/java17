package com.valuelab.common.validation;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

@GroupSequence({ Default.class, FullValidationGroupFirstOrder.class, FullValidationGroupSecondOrder.class })
public interface FullValidationGroup {
}
