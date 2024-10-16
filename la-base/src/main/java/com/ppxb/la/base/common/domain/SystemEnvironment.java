package com.ppxb.la.base.common.domain;

import com.ppxb.la.base.common.enumeration.SystemEnvironmentEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SystemEnvironment {

    private boolean isProd;

    private String projectName;

    private SystemEnvironmentEnum currentEnvironment;
}
