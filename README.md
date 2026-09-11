# Delight o' Flight Fix（delighto_flight_fix）

## 简介

本模组是 [Delight o' Flight](https://www.curseforge.com/minecraft/mc-mods/delighto-flight) 的修复附属模组。
Delight o' Flight 的「Arc（电弧）」效果会电击周围的生物，但原作没有任何实体保护，导致被动生物（村民、牛羊等）经常被误杀。
因此本模组只做一件事，为其加入一套可配置的黑名单机制，让整合包作者可以决定哪些生物不会被电。

## 前置依赖

- Farmer's Delight（农夫乐事）1.20.1-1.3.1
- Delight o' Flight（云端之乐）1.20.1-1.3.4

## 功能

黑名单（实体 ID 或实体标签）同时作用于三个层面，实现彻底的拦截：

1. 黑名单生物【持有】Arc 效果时 —— 干脆不生成电弧（闪电）。
2. 黑名单生物【作为受害者】时 —— 不被电击（无伤害、无音效）。
3. 客户端渲染层 —— 不向黑名单生物绘制电弧（无视觉）。

## 配置

配置文件位于：`config/delighto_flight_fix-common.toml`

- `shockBlacklist`：字符串列表。每项可以是：
  - 实体类型 ID，例如 `minecraft:villager`
  - 实体类型标签（以 `#` 开头），例如 `#minecraft:raiders`
  非法条目会被忽略。默认已包含村民、流浪商人、牛羊猪鸡、猫狗马、豹猫、行商羊驼、铁傀儡、雪傀儡、僵尸马、骷髅马、北极熊、嗅探兽、炽足兽、蝌蚪、青蛙、骆驼、海豚、山羊、美西螈、云羚等 35 种被动生物。

## 开发 / 构建

- 开发环境启动：`./gradlew runClient`（或 `genIntellijRuns` 后用 IDE 运行）。
- 打包：`./gradlew jar`，产物在 `build/libs/`。

## 技术要点（维护必读）

本模组用 Mixin 修改 Delight o' Flight 的类，涉及三处对第三方模组类的注入，
有几处非显而易见的坑，已处理如下：

1. **Mixin 目标**（`src/main/java/com/garam/DelightoFlightFix/mixin/`）：
   - `ArcEffectMixin`：注入 `ArcEffect.applyEffectTick`，命中黑名单则取消生成电弧。
   - `ElectricCurrentMixin`：重定向 `ElectricCurrent.tick` 里的 `getEntitiesOfClass`，过滤受害者。
   - `ElectricCurrentRendererMixin`（client）：重定向渲染器 `getTargets` 的目标查询，过滤电弧视觉。

2. **Mixin 注册**：Forge 1.20.1 不支持 `mods.toml` 的 `[[mixins]]` 段。
   开发环境靠 run 配置里的 `--mixin.config` 参数注册；生产 jar 靠 manifest 的 `MixinConfigs` 属性。


3. **本模组 refmap 为手写**（`delighto_flight_fix.refmap.json`）：
   Mixin 注解处理器无法对「第三方模组类」生成 refmap（它只在原版 SRG 映射里做直接查找）。
   因此本模组的 refmap 是手动编写的，SRG 名从混淆后的 Delight o' Flight jar 反编译确认。
   - `ArcEffect.applyEffectTick` -> `m_6742_`
   - `ElectricCurrent.tick` -> `m_8119_`
   - `Level.getEntitiesOfClass` -> `m_45976_`

   注意 refmap 键有**两种格式**，写错会导致生产环境报 `failed injection check (0/1)`：
   - `method` 属性（如 `tick`、`applyEffectTick`）用**方法名**当键。
   - `@At` 的 INVOKE 目标（如 `getEntitiesOfClass`）用**完整方法引用**（`Lowner;name(desc)ret`）当键。

   若升级 Delight o' Flight 版本，需重新核对这些 SRG 名并更新 refmap。

## 代码结构

- `com.garam.DelightoFlightFix.DelightoFlightFix`：主类，注册配置。
- `com.garam.DelightoFlightFix.Config`：黑名单配置与判定逻辑（`isShockBlacklisted`）。
- `com.garam.DelightoFlightFix.mixin`：三个 Mixin 类。

## 许可 / 鸣谢

- 本模组作者：Garam
- Delight o' Flight：© cloudmeow，本模组仅做兼容修复，不对其代码做任何修改再分发。
