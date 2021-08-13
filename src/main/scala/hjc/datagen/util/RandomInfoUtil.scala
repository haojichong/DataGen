package hjc.datagen.util

object RandomInfoUtil {
  // 数据模板
  private lazy val base = "0123456789abcdefghijklmnopqrstuvwxyz"
  private lazy val firstName = "阿哀蔼爱昂敖奥艾安把巴仈拜百办板班般邦暴宝保褒孛北卑本奔鼻辟弊闭碧比毴逼表别丙帛薄伯波不布步补卜宾卞边毕贝鲍包柏白财彩藏仓苍厕策茶察畅唱敞晁巢朝潮钞超抄沉晨尘郴承城称匙持池迟茌赤齿崇宠充雏触处初传钏闯淳春啜慈从枞醋爨翠催寸村错蹉郗苌崔丛褚楚储程呈成车常昌柴曹岑操蔡采陈达答笪大代逮歹但丹党到道稻岛刀德登迪地第底磾氐店典迭定栋冻东冬豆斗独毒度蠹笃督都端对兑顿敦撉铎朵多遆底戴段端木杜堵窦刁狄邓丁董娥鹅鄂尔兒贰饭藩鲂芳肥斐飞酆逢奉风封佛洑扶浮福付富复甫府夫番臭傅符凤丰费方范樊法房冯串尕改淦刚告郜皋革盖庚拱共弓公功恭勾苟缑估贯官广光诡圭妫国虢果芥郐呙管过桂贵归关顾谷古辜贡巩龚耿葛戈干宫甘郭高堕哈亥海邗函寒汉汗罕蒿盍禾合和河赫黑衡弘红宏闳郈厚候狐斛户扈虎呼忽滑花怀桓皇回辉浑荤火霍惠皇甫宦环华后侯洪贺郝杭韩何胡黄集籍及汲极姞计蓟暨冀己稽几箕鸡姬机郏甲家嘉加佳建剑检剪翦蹇坚菅绛将缴郊胶竭节捷颉介接进靳敬靖镜井京九酒局巨俱剧驹鞠卷角军君菌近居景荆经晋金斤解揭滘矫焦蒋姜江简贾季纪戢吉嵇凯开撖坎亢抗靠考克客可空库苦狂邝况旷魁夔蒉厍匡蒯寇孔柯康阚瘳腊喇拉莱兰浪朗牢老勒类磊犁力历利励隶荔栗礼里理莲联敛良粱聊列烈零令留六泷隆漏芦逯禄律旅甪里栾伦洛雒裸率陇骆吕闾路鹿陆鲁卢楼娄龙柳凌蔺廖练廉连郦厉黎冷雷乐劳郎蓝赖来梁罗林刘李佴蛮漫满芒莽猫茆么枚门蒙弥糜秘密米苗妙蔑闽悯敏谬磨摩墨抹撚牟木睦慕母穆莫缪明闵宓孟梅茅毛麦麻马丑纳奈那赧难泥能鸟念耨乜诺你农钮牛甯宁聂年倪南偶欧阳欧苻盘旁胖朋蓬鹏甓匹偏朴频凭繁仆蒲普溥浦濮平皮彭裴逄庞潘次矫琚圈区骑琦亓其奇歧祈旗泣乞杞柒七期乾潜前茜浅千牵骞侨桥谯樵巧茄伽琴禽勤庆苘倾青卿清邛求秋渠麹曲麴全权泉雀却炔覃顼阙璩屈裘邱丘秦钦乔强綦齐祁漆戚瞿仇钱冉让热人仁壬仍荣容融柔汝锐瑞芮阮茹戎任饶穰产晟隽洒撒赛塞伞三桑森僧杀善陕闪苫山赏商韶少绍蛇折设神审姺绳圣声时实莳势士世示侍是释奭始尸失诗蓍受守术树暑疏书殳叔耍双霜税舜顺硕说似佀姒俟肆四死司松嵩玊素速宿粟酸隋随岁穗邃眭睢狲锁所舍索苏宋斯思司徒司马水帅束舒寿首史石施师盛生慎沈申屠申佘尚上官沙萨邵单谌孙淡澹荡调塔他台邰太泰潭檀堂腾藤提题甜帖廷茼同佟彤桐通凃土陀妥庹脱屠涂童铁田滕陶汤谭谈钭唐瓦完宛望惟尉位伟尾委隗巍威问瓮翁沃我无毋兀务戊悟仵午忤五屋乌武伍吾吴吳巫邬翁闻人闻文温魏卫韦危汪万王除袛郄隰戏系郤喜夕西希昔析息悉鏬闲贤咸县线羡霰先鲜仙庠象乡襄香效効校孝肖邪偰解写信衅刑行性兴星休修飍续绪婿玄旋轩学穴雪郇秀荀寻薛禤宣轩辕许须胥熊幸姓邢辛忻谢萧项向相冼夏席奚习徐澄剡蔚吁牙亚鸦押闫沿延言盐燕妟烟焉扬洋养药咬要曳业冶野耶仪夷宜移义弋益裔抑以矣蚁扆乙一衣壹银訚隐阴音盈营英婴用永勇雍由油犹右友有酉鱼愚玉御遇豫愈僪宇羽禹语原源辕苑渊越月云妘员运允恽悦元喻郁庾虞俞於游尤应印尹易伊姚尧仰杨阳羊晏颜阎严鄢余岳叶袁于夭邶邲贲匕邴漕虿禅长呈虫兹邸鬭俸祭谏謇佼咎耒邙祢麋弭貊钼蘧穰鄯莘夙仝镡筵顏琰徭幺怡奕羿鄞嬴鄘宥攸臾毓鬱庾於贠爰恽在载再宰赞昝皂造枣早遭糟泽曽扎窄斋绽战展粘占鳣胀仉掌漳召兆诏照肇钊招柘者镇枕针真甄正政征蒸植直郅挚秩彘智陟致芷止之枝芝脂只终众种中衷纣舟竹烛住主颛壮禚濯字子紫訾资纵鄹俎祖尊笮柞佐足左邹宗自卓庄祝竺诸葛诸朱州仲锺钟支赵章湛詹翟曾迮臧查郑周张何吕施张孔陶姜戚谢邹喻柏水窦章云苏赵钱曹严华金魏孙李周吴郑王杨朱秦尤许潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳酆鲍史冯陈褚卫蒋沈韩唐费廉岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅皮卞齐康伍余元卜顾孟平黄和穆蔡田樊胡凌霍虞万支柯咎管卢莫经房裘缪干解应宗宣丁贲邓郁单杭洪包驷公良拓拔夹谷宰父谷粱晋楚阎法汝鄢涂钦段干百里东郭南门呼延归海羊舌微生岳帅缑亢况后有琴梁丘左丘东门西门商牟佘佴伯赏南宫墨哈谯笪年爱阳佟第五言福百家诸左石崔吉钮龚程嵇邢滑裴陆荣翁荀羊於惠甄魏加封芮羿储靳汲邴糜萧尹姚邵湛汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞熊纪舒屈项祝董梁杜阮蓝闵席季麻强贾路娄危江童颜郭梅盛林刁钟徐邱骆高夏松井段富巫乌焦巴弓牧隗山谷车侯宓蓬全郗班仰秋仲伊宫宁仇栾暴甘钭厉戎祖武符刘姜詹束龙叶幸司韶郜黎蓟薄印宿白怀蒲台从鄂索咸籍赖卓蔺屠蒙池乔阴郁胥能苍双闻莘党翟谭贡劳逄姬申扶堵冉宰郦雍却璩桑桂濮牛寿通边扈燕冀郏浦尚农温别庄晏柴瞿阎充慕连茹习宦艾鱼容向古易慎戈廖庚终暨居衡步都耿满弘匡国文寇广禄阙东殴殳沃利蔚越夔隆师巩厍聂晁勾敖融冷訾辛阚那简饶空曾毋沙乜养鞠须丰巢关蒯相查后江红游竺权逯盖益桓公万俟司马上官欧阳夏侯诸葛闻人东方赫连皇甫尉迟公羊澹台公冶宗政濮阳淳于仲孙太叔申屠公孙乐正轩辕令狐钟离闾丘长孙慕容鲜于宇文司徒司空亓官司寇仉督子车颛孙端木巫马公西漆雕乐正壤姓续"
  private lazy val girl = "雅芝惠珠翠莲素云秀娟英真环雪荣爱妹霞香月莺媛艳瑞凡玉萍菊兰凤洁梅琳华红娥月莺媛艳瑞壮伟刚勇毅玲芬芳燕彩春慧巧美娜静淑佳嘉琼勤珍贞莉桂婉蓓炎德行娴瑾颖露瑶怡婵雁蓓壮琛钧冠泰盛雄梁栋维启纨仪荷丹德行时泰盛雄梁栋维启家致琬茗羽希宁欣飘育滢馥筠柔竹霭凝晓欢霄蓉眉君琴蕊薇菁梦岚苑婕馨珊莎锦黛青雅芝惠珠翠莲素云秀娟英真环雪荣爱妹霞香瑗琰韵融娣叶璐娅琦晶妍茜秋珊莎锦黛青雅芝惠珠翠莲素云秀娟英真环雪荣爱妹霞香月莺媛艳瑞凡玉萍菊兰凤洁梅琳华红娥玲芬芳燕彩春慧巧美娜静淑倩婷姣园艺咏卿聪澜纯毓悦昭冰爽枫芸菲寒伊亚宜可姬舒影荔枝思丽 "
  private lazy val boy = "星光天达安岩平保东文辉凤洁梅琳华红娥力明永健世广振英真环雪荣爱妹霞香月莺媛艳瑞壮伟刚勇毅俊峰若鸣朋斌梁栋维启家致永健世广振壮伟刚勇毅俊峰若鸣树桂婉娴瑾颖露瑶怡婵雁蓓炎德行时泰盛雄梁栋维启家致永健世广振壮琛钧冠泰盛雄梁栋维启策腾楠榕风航弘强军发武新利清飞彬富顺信子杰涛昌成康启家致琬茗羽希宁欣飘育滢馥筠柔竹霭凝晓欢霄会思群豪心邦承乐绍功志义兴良海山仁波宁贵福生龙元全国胜学祥才中茂进林有坚和彪亮政谦亨奇固之轮翰博诚先敬震松善厚庆震松善士以建诚先敬震克伦翔旭鹏泽晨辰诚先敬震松善士以建士以建诚先敬震松善士以建诚先敬震松善厚庆震松善士以建诚先敬震磊民友厚庆磊民友裕河哲江超浩朗伯宏言"
  private lazy val email_suffix = "@msn.com,@ask.com,@live.com,@0355.net,@3721.net,@haima.me,@qq.com,@163.com,@126.com,@163.net,@188.com,@yeah.net,@gmail.com,@googlemail.com,@hotmail.com,@yahoo.com,@yahoo.com.cn,@sina.com,@sohu.com,@tom.com,@21cn.com,@qq.com,@263.net,@189.cn,@139.com,@eyou.com,@sogou.com,@hongkong.com,@ctimail.com,@hknet.com,@netvigator.com,@mail.hk.com,@swe.com.hk,@ITCCOLP.COM.HK,@BIZNETVIGATOR.COM,@SEED.NET.TW,@TOPMARKEPLG.COM.TW,@PCHOME.COM.TW,@libero.it,@terra.es,@webmail.co.za,@xtra.co.nz,@pacific.net.sg,@FASTMAIL.FM,@emirates.net.ae,@eim.ae,@net.sy,@scs-net.org,@mail.sy,@ttnet.net.tr,@superonline.com,@yemen.net.ye,@y.net.ye,@cytanet.com.cy,@aol.com"
    .split(",")
  private lazy val telFirst: Array[String] = "181,189,199,130,131,132,145,155,133,149,153,173,177,180,156,166,171,175,176,185,186,166,134,135,136,137,138,139,147,150,151,152,157,158,159,172,178,182,183,184,187,188,198".
    split(",")
  private var name_sex = ""

  // 获取随机数[start,end]
  def getNum(start: Int, end: Int): Int = (Math.random * (end - start + 1) + start).toInt

  def getEmail(min: Int, max: Int) = {
    val email: StringBuffer = new StringBuffer()
    for (_ <- 0 until getNum(min, max)) {
      val number = (Math.random() * base.length()).toInt
      email.append(base.charAt(number.toInt))
    }
    email.append(email_suffix((Math.random() * email_suffix.length).toInt)).toString()
  }

  def getTelephone() = {
    telFirst(getNum(0, telFirst.length - 1)) + String.valueOf(getNum(1, 888) + 10000).substring(1) + String.valueOf(getNum(1, 9100) + 10000).substring(1)
  }

  def getIpAddr() = {
    s"${getNum(0, 255)}.${getNum(0, 255)}.${getNum(0, 255)}.${getNum(0, 255)}"
  }

  def getChineseName(): String = {
    var index = getNum(0, firstName.length() - 1)
    var str = boy
    var length = boy.length()
    val first = firstName.substring(index, index + 1)
    if (getNum(0, 1) == 0) {
      str = girl
      length = girl.length()
      name_sex = "女"
    } else {
      name_sex = "男"
    }
    index = getNum(0, length - 1)
    val two = str.substring(index, index + 1)
    val hasThird = getNum(0, 1)
    var third = ""
    if (hasThird == 1) {
      index = getNum(0, length - 1)
      third = str.substring(index, index + 1)
    }
    first + two + third
  }

  def main(args: Array[String]): Unit = {
    println(getChineseName())
    println(getIpAddr())
    println(getEmail(3, 18))
  }
}

