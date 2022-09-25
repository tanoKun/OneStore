# OneStore

自動で売値、買値を調節してくれるShopPL<br>
minecraft v1.18.2<br>

### authors: [tanokun](https://github.com/tanoKun)

## Depend (Plugin)
- [CommandAPI v8.5.1](https://github.com/JorelAli/CommandAPI)
- [Jecon v2.2.1](https://github.com/HimaJyun/Jecon)
- [HolographicDisplay v3.0.0-SNAPSHOT](https://dev.bukkit.org/projects/holographic-displays/files/3686457)

## Libraries
 - **HolographicDisplay** 3.0.0-SNAPSHOT
 - **Jecon** 2.2.1
 - **CommandAPI** 8.5.1
 - **HikariCP** 5.0.1
 - **InvUI** 0.8
 - **exp4j** 0.4.8

## Commands
- **/ostore create {id} {元価格} {ストック} {変化幅}** 
- **/ostore delete {id}**
- **/ostore list**
- **/ostore set baseprice {id} {元価格}**
- **/ostore set buyprice {id} {買値}**
- **/ostore set sellprice {id} {売値}**
- **/ostore set stock {id} {ストック}**
- **/ostore set change {id} {変化幅}**
- **/ostore set expression {id} {SELL_PLUS} {expression}**
- **/ostore set expression {id} {SELL_SUBTRACT} {expression}**
- **/ostore set increase {id} {true | false}**

## 追加予定

- **/ostore run ....** の追加
- 看板での売買の追加