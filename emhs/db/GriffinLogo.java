package emhs.db;

import emhs.db.components.UIClosedShape;
import emhs.db.components.UIComponent;
import emhs.db.util.RenderProcedure;
import emhs.db.util.UIFace;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

public class GriffinLogo extends UIClosedShape {
    protected AffineTransform transform;

    static {
        UIFace.addRenderProcedure(GriffinLogo.class, new RenderProcedure() {
            private void paintShapeNode_0_0_0(Graphics2D g) {
                GeneralPath shape0 = new GeneralPath();
                shape0.moveTo(133.9, 0.0);
                shape0.curveTo(104.96573, 22.00778, 64.616196, 25.90182, 29.599995, 21.55);
                shape0.curveTo(21.449856, 25.81894, 7.8350525, 33.92156, -5.722046E-6, 38.75);
                shape0.curveTo(14.913912, 65.37898, 23.850822, 94.21656, 20.699995, 124.7);
                shape0.curveTo(18.74648, 146.37619, 9.774171, 164.69188, 5.2999954, 185.85);
                shape0.curveTo(-2.2199807, 214.59273, -2.0796247, 246.36018, 11.699995, 273.3);
                shape0.curveTo(26.508884, 301.4003, 49.005013, 327.10995, 79.399994, 339.15);
                shape0.curveTo(98.781494, 345.9357, 121.14215, 352.9891, 133.9, 370.5);
                shape0.curveTo(146.65785, 352.98914, 169.0185, 345.93573, 188.4, 339.15);
                shape0.curveTo(218.795, 327.10995, 241.29109, 301.40033, 256.1, 273.3);
                shape0.curveTo(269.8796, 246.36017, 270.01996, 214.59271, 262.5, 185.84999);
                shape0.curveTo(258.02582, 164.69186, 249.05351, 146.37619, 247.09999, 124.69999);
                shape0.curveTo(243.94916, 94.21655, 252.88608, 65.37897, 267.8, 38.749992);
                shape0.curveTo(259.96494, 33.92155, 246.35013, 25.818932, 238.19998, 21.549992);
                shape0.curveTo(203.18378, 25.901812, 162.83435, 22.007772, 133.89998, -7.6293945E-6);
                shape0.closePath();
                shape0.moveTo(133.95, 7.6);
                shape0.curveTo(161.4365, 28.84, 204.26, 31.6, 237.6, 27.4);
                shape0.curveTo(245.36, 31.52, 253.14, 35.69, 260.6, 40.35);
                shape0.curveTo(246.40001, 66.05, 238.55, 95.68, 241.55, 125.1);
                shape0.curveTo(243.41, 146.02, 252.04001, 165.43, 256.3, 185.85);
                shape0.curveTo(263.46, 213.59001, 263.31998, 244.20001, 250.19998, 270.2);
                shape0.curveTo(236.09998, 297.32, 214.73999, 322.13, 185.79999, 333.75);
                shape0.curveTo(167.99998, 341.95, 146.0344, 344.8, 133.94998, 361.7);
                shape0.curveTo(121.865585, 344.80002, 99.89999, 341.95, 82.09999, 333.75);
                shape0.curveTo(53.15999, 322.13, 31.799992, 297.32, 17.69999, 270.2);
                shape0.curveTo(4.5799894, 244.20001, 4.439989, 213.59001, 11.599989, 185.85);
                shape0.curveTo(15.859989, 165.43001, 24.48999, 146.02, 26.349989, 125.100006);
                shape0.curveTo(29.349989, 95.68001, 21.499989, 66.05, 7.2999897, 40.350006);
                shape0.curveTo(14.75999, 35.690006, 22.53999, 31.520006, 30.29999, 27.400005);
                shape0.curveTo(63.63999, 31.600006, 106.463486, 28.840006, 133.94998, 7.600006);
                shape0.closePath();
                g.fill(shape0);
            }

            private void paintShapeNode_0_0_1(Graphics2D g) {
                GeneralPath shape1 = new GeneralPath();
                shape1.moveTo(54.125, 103.25);
                shape1.curveTo(54.35, 112.55, 59.275, 120.75, 65.125, 127.625);
                shape1.curveTo(78.625, 143.175, 95.4, 155.575, 113.5, 165.25);
                shape1.curveTo(92.25, 160.125, 74.275, 146.7375, 59.875, 130.6875);
                shape1.curveTo(67.2, 145.6375, 77.3, 159.325, 89.875, 170.25);
                shape1.curveTo(96.325, 176.125, 105.3, 178.0125, 112.0, 183.5625);
                shape1.curveTo(93.875, 178.2375, 78.475, 166.6125, 67.25, 151.5625);
                shape1.curveTo(67.7, 155.2625, 66.775, 158.6125, 64.375, 161.4375);
                shape1.curveTo(68.55, 171.9125, 75.3875, 181.3, 84.0625, 188.5);
                shape1.curveTo(91.0875, 194.575, 100.675, 195.6875, 109.0, 199.1875);
                shape1.curveTo(98.975, 201.1125, 89.675, 196.5875, 81.375, 191.5625);
                shape1.curveTo(82.55, 196.8625, 85.45, 201.725, 90.5, 204.125);
                shape1.curveTo(98.475, 209.675, 108.6, 207.0375, 117.5, 209.4375);
                shape1.curveTo(110.525, 210.8125, 103.3625, 210.8625, 96.3125, 210.3125);
                shape1.curveTo(100.3125, 221.2625, 112.775, 217.8125, 121.75, 219.3125);
                shape1.curveTo(117.425, 219.8375, 113.1, 220.3125, 108.75, 220.6875);
                shape1.curveTo(111.1, 232.2125, 123.94997, 229.8, 132.625, 229.625);
                shape1.curveTo(136.9, 237.025, 140.3, 246.1125, 148.625, 249.9375);
                shape1.curveTo(157.3, 251.5375, 164.0125, 244.3125, 170.5625, 239.9375);
                shape1.curveTo(167.8875, 240.0875, 162.4875, 240.4125, 159.8125, 240.5625);
                shape1.curveTo(165.8875, 236.9125, 172.125, 233.25, 176.75, 227.75);
                shape1.curveTo(174.175, 228.075, 169.0125, 228.7, 166.4375, 229.0);
                shape1.curveTo(171.06248, 225.35, 175.825, 223.76875, 179.375, 219.09375);
                shape1.curveTo(177.83035, 216.4384, 177.15585, 215.231, 176.0, 212.40625);
                shape1.curveTo(171.725, 212.33125, 163.5, 210.3376, 159.25, 210.1875);
                shape1.curveTo(159.2, 206.6375, 160.25, 203.425, 163.125, 201.125);
                shape1.curveTo(158.075, 200.225, 153.7, 197.625, 149.5, 194.875);
                shape1.curveTo(149.675, 192.075, 151.09999, 190.1875, 153.75, 189.3125);
                shape1.curveTo(152.15, 186.1125, 149.8875, 183.1875, 149.4375, 179.5625);
                shape1.curveTo(150.9375, 178.6375, 153.875, 176.7375, 155.375, 175.8125);
                shape1.curveTo(152.9, 172.2625, 150.0125, 169.0625, 146.9375, 166.0625);
                shape1.curveTo(154.0875, 167.5875, 157.8875, 171.025, 160.0625, 177.75);
                shape1.curveTo(156.3256, 177.82487, 154.48125, 181.475, 152.53125, 181.0);
                shape1.curveTo(155.14343, 185.15627, 154.98067, 186.21355, 159.0625, 189.78125);
                shape1.curveTo(157.1375, 191.53125, 153.8375, 192.06876, 151.9375, 193.84375);
                shape1.curveTo(155.10023, 196.45612, 164.59949, 198.83736, 166.9375, 200.625);
                shape1.curveTo(165.6875, 202.525, 163.625, 204.5375, 162.375, 206.4375);
                shape1.curveTo(167.5, 208.2375, 172.775, 209.775, 178.25, 210.125);
                shape1.curveTo(180.77501, 215.575, 184.3625, 223.125, 189.9375, 226.0);
                shape1.curveTo(191.52515, 220.55026, 195.7125, 219.8625, 199.8125, 220.3125);
                shape1.curveTo(204.7625, 225.3625, 209.2625, 231.8375, 216.5625, 233.5625);
                shape1.curveTo(220.43748, 234.7625, 225.275, 236.8125, 228.125, 232.5625);
                shape1.curveTo(234.425, 232.5875, 241.2375, 231.65, 246.6875, 235.5);
                shape1.curveTo(251.7875, 235.275, 256.9, 234.5125, 261.75, 232.8125);
                shape1.curveTo(264.40457, 234.61147, 267.16452, 236.22108, 270.09375, 237.59375);
                shape1.curveTo(276.39276, 232.13092, 272.11804, 227.00018, 273.78125, 221.5625);
                shape1.curveTo(272.88095, 217.9963, 271.44998, 208.825, 264.0, 209.875);
                shape1.curveTo(260.1, 212.125, 256.725, 215.225, 253.0, 217.75);
                shape1.curveTo(237.97499, 218.875, 221.6875, 215.0375, 211.0625, 203.6875);
                shape1.curveTo(215.4125, 206.4875, 220.0375, 209.56876, 224.9375, 211.34375);
                shape1.curveTo(224.68748, 203.09375, 222.08826, 200.57784, 217.5, 188.3125);
                shape1.curveTo(214.875, 188.6375, 214.35, 188.5875, 211.75, 188.9375);
                shape1.lineTo(214.28125, 191.0625);
                shape1.curveTo(212.73125, 190.3875, 204.1875, 188.9375, 204.1875, 188.9375);
                shape1.lineTo(206.96875, 186.875);
                shape1.lineTo(213.25, 186.9375);
                shape1.curveTo(213.2, 185.5625, 213.17503, 185.875, 213.125, 184.5);
                shape1.lineTo(218.09375, 184.46875);
                shape1.curveTo(216.49374, 169.9687, 200.3375, 163.7375, 192.3125, 153.5625);
                shape1.curveTo(192.1125, 150.7125, 191.9875, 147.9125, 191.9375, 145.0625);
                shape1.curveTo(193.43748, 144.0125, 196.375, 141.925, 197.875, 140.875);
                shape1.curveTo(196.775, 139.6, 194.6625, 137.025, 193.5625, 135.75);
                shape1.curveTo(197.1375, 136.45, 200.4625, 137.7625, 203.5625, 139.6875);
                shape1.curveTo(205.2875, 138.2375, 206.9875, 136.7375, 208.6875, 135.1875);
                shape1.curveTo(203.1375, 129.0125, 193.1, 126.3375, 185.375, 129.9375);
                shape1.curveTo(184.875, 126.4125, 184.425, 122.9, 183.875, 119.375);
                shape1.curveTo(178.3, 121.9, 180.325, 128.55, 180.375, 133.375);
                shape1.curveTo(176.09999, 130.475, 172.375, 127.0125, 169.25, 122.9375);
                shape1.curveTo(163.15, 128.5375, 167.05, 136.6375, 167.375, 143.5625);
                shape1.curveTo(162.775, 149.4625, 157.0875, 156.3375, 159.6875, 164.4375);
                shape1.lineTo(164.625, 165.5);
                shape1.curveTo(165.12498, 167.85, 166.06248, 172.5875, 166.5625, 174.9375);
                shape1.curveTo(167.7375, 172.6125, 170.03749, 167.95, 171.1875, 165.625);
                shape1.curveTo(170.5125, 170.175, 169.76251, 174.7, 168.5625, 179.125);
                shape1.curveTo(162.33751, 172.225, 158.475, 163.0, 150.125, 158.25);
                shape1.curveTo(131.34988, 147.45, 109.1, 145.5125, 90.0, 135.4375);
                shape1.curveTo(75.175, 128.4125, 63.15, 116.825, 54.125, 103.25);
                shape1.closePath();
                shape1.moveTo(169.5, 128.15625);
                shape1.curveTo(174.30511, 136.37364, 182.6513, 133.52191, 172.75, 139.9375);
                shape1.curveTo(168.5944, 142.8837, 168.81656, 129.97615, 169.5, 128.15625);
                shape1.closePath();
                shape1.moveTo(179.75, 154.9375);
                shape1.curveTo(180.325, 156.4125, 181.425, 159.3375, 182.0, 160.8125);
                shape1.curveTo(182.87502, 160.5625, 184.66249, 160.1, 185.5625, 159.875);
                shape1.curveTo(184.81248, 163.675, 184.075, 167.5125, 183.25, 171.3125);
                shape1.curveTo(178.6, 166.9625, 179.375, 160.7125, 179.75, 154.9375);
                shape1.closePath();
                shape1.moveTo(136.125, 207.625);
                shape1.curveTo(137.875, 215.075, 140.6125, 223.5125, 149.0625, 225.5625);
                shape1.curveTo(146.6125, 225.3625, 141.7, 224.95, 139.25, 224.75);
                shape1.curveTo(142.575, 228.1, 146.3125, 231.125, 150.6875, 233.0);
                shape1.curveTo(149.5625, 233.175, 147.275, 233.45, 146.125, 233.625);
                shape1.curveTo(147.025, 234.525, 148.8125, 236.3875, 149.6875, 237.3125);
                shape1.curveTo(151.0125, 240.5375, 152.325, 243.75, 153.625, 247.0);
                shape1.curveTo(149.4, 245.425, 144.9375, 243.75, 143.5625, 239.0);
                shape1.curveTo(138.9875, 229.475, 126.275, 218.025, 136.125, 207.625);
                shape1.closePath();
                g.fill(shape1);
            }

            private void paintShapeNode_0_0_2(Graphics2D g) {
                GeneralPath shape2 = new GeneralPath();
                shape2.moveTo(220.25981, 185.90102);
                shape2.curveTo(219.57191, 188.23332, 219.48915, 188.45189, 218.23859, 189.80856);
                shape2.curveTo(216.21901, 191.82512, 222.35545, 191.36652, 224.01909, 192.79529);
                shape2.curveTo(224.0929, 190.3341, 222.09543, 187.57637, 220.25981, 185.90102);
                shape2.closePath();
                g.fill(shape2);
            }

            private void paintShapeNode_0_0_3(Graphics2D g) {
                GeneralPath shape3 = new GeneralPath();
                shape3.moveTo(126.99, 52.79);
                shape3.curveTo(128.75, 52.79, 130.03148, 52.94535, 130.66365, 53.31033);
                shape3.curveTo(132.16531, 54.177315, 132.8, 56.23, 132.92, 57.98);
                shape3.curveTo(130.34999, 57.66, 127.57, 56.67, 125.17, 58.12);
                shape3.curveTo(125.45, 58.7, 126.009995, 59.85, 126.29, 60.42);
                shape3.curveTo(126.37, 59.859997, 126.51, 58.75, 126.58, 58.199997);
                shape3.curveTo(128.36, 58.299995, 130.14, 58.399998, 131.92, 58.499996);
                shape3.lineTo(131.93, 59.219997);
                shape3.curveTo(131.5, 59.219997, 130.65, 59.229996, 130.23, 59.239998);
                shape3.lineTo(130.47, 60.519997);
                shape3.curveTo(128.19, 60.159996, 125.950005, 60.449997, 123.93, 61.619995);
                shape3.curveTo(122.840004, 61.079994, 122.11405, 60.281757, 121.10181, 59.54737);
                shape3.curveTo(121.1918, 58.81737, 120.930016, 57.819996, 121.01001, 57.089996);
                shape3.curveTo(121.95001, 54.109997, 127.600006, 56.67, 126.99001, 52.789997);
                shape3.closePath();
                g.fill(shape3);
            }

            private void paintShapeNode_0_0_4(Graphics2D g) {
                GeneralPath shape4 = new GeneralPath();
                shape4.moveTo(140.26251, 52.282806);
                shape4.curveTo(139.22679, 53.462845, 140.04349, 55.356926, 138.0201, 54.837395);
                shape4.curveTo(135.74739, 53.973106, 138.93251, 52.392807, 140.26251, 52.282806);
                shape4.closePath();
                g.fill(shape4);
            }

            private void paintShapeNode_0_0_5(Graphics2D g) {
                GeneralPath shape5 = new GeneralPath();
                shape5.moveTo(141.24667, 55.81487);
                shape5.curveTo(142.01079, 54.91395, 143.55394, 53.94543, 145.14232, 54.820217);
                shape5.curveTo(142.9421, 55.483772, 143.76144, 56.81076, 142.8594, 57.358177);
                shape5.curveTo(142.31209, 56.604618, 142.18045, 56.437317, 141.24669, 55.81487);
                shape5.closePath();
                g.fill(shape5);
            }

            private void paintShapeNode_0_0_6(Graphics2D g) {
                GeneralPath shape6 = new GeneralPath();
                shape6.moveTo(135.59, 55.16);
                shape6.curveTo(136.39258, 55.632435, 137.3401, 56.33483, 138.80745, 56.135056);
                shape6.curveTo(141.49509, 56.09251, 142.91, 59.34, 144.44, 61.43);
                shape6.curveTo(141.85, 62.72, 140.8548, 64.38262, 141.6848, 67.42262);
                shape6.curveTo(142.1048, 66.96262, 141.99011, 67.0048, 142.40012, 66.554794);
                shape6.curveTo(143.41467, 71.529594, 142.5, 74.71, 142.44, 79.09);
                shape6.curveTo(140.45, 81.71, 137.68001, 84.64, 134.02, 83.46999);
                shape6.curveTo(134.02, 81.05999, 133.4045, 79.27316, 131.9446, 76.112335);
                shape6.curveTo(133.31459, 75.682335, 136.02, 76.18999, 136.49, 74.45999);
                shape6.curveTo(138.29001, 67.94999, 132.33, 61.54999, 135.59001, 55.159992);
                shape6.closePath();
                g.fill(shape6);
            }

            private void paintShapeNode_0_0_7(Graphics2D g) {
                GeneralPath shape7 = new GeneralPath();
                shape7.moveTo(151.9001, 82.75535);
                shape7.curveTo(153.54958, 82.27188, 156.02057, 83.38454, 157.02103, 84.72541);
                shape7.curveTo(155.78656, 84.17053, 153.63753, 84.35576, 153.21414, 85.47966);
                shape7.curveTo(152.8862, 84.39043, 152.69633, 83.88006, 151.9001, 82.75534);
                shape7.closePath();
                g.fill(shape7);
            }

            private void paintShapeNode_0_0_8(Graphics2D g) {
                GeneralPath shape8 = new GeneralPath();
                shape8.moveTo(71.82, 90.98);
                shape8.curveTo(72.19, 88.11, 75.1, 84.71001, 78.229996, 86.07001);
                shape8.curveTo(83.1, 87.96001, 86.45, 92.29001, 89.159996, 96.560005);
                shape8.curveTo(89.74, 96.520004, 90.899994, 96.450005, 91.479996, 96.41);
                shape8.curveTo(91.78, 96.94, 92.39, 98.01, 92.689995, 98.55);
                shape8.curveTo(93.329994, 98.5, 93.979996, 98.450005, 94.619995, 98.41);
                shape8.curveTo(97.399994, 103.18, 103.99999, 105.9, 108.96999, 102.850006);
                shape8.curveTo(113.45999, 100.26001, 114.80988, 94.44263, 115.83988, 89.81263);
                shape8.curveTo(116.15988, 90.28263, 117.194756, 91.16765, 118.78932, 92.0775);
                shape8.curveTo(120.20932, 91.4875, 120.55718, 89.90784, 121.97718, 89.30783);
                shape8.curveTo(125.35718, 90.34783, 125.84999, 95.450005, 129.65, 95.64001);
                shape8.curveTo(126.979996, 99.78001, 121.91497, 103.30994, 122.64497, 108.85994);
                shape8.curveTo(121.294975, 107.54994, 119.85999, 106.07001, 118.439995, 104.84001);
                shape8.curveTo(121.59, 108.280014, 124.20999, 113.84001, 121.24999, 118.18001);
                shape8.curveTo(118.649994, 123.72001, 110.26999, 124.23001, 109.65999, 130.97);
                shape8.curveTo(110.04999, 132.09, 110.429985, 133.2, 110.80999, 134.32);
                shape8.curveTo(112.19999, 134.34001, 113.58999, 134.36, 114.97999, 134.38);
                shape8.curveTo(115.62765, 135.93663, 115.81877, 137.10449, 115.65999, 138.47673);
                shape8.curveTo(114.929985, 138.49673, 113.47999, 138.77, 112.749985, 138.79);
                shape8.curveTo(110.57999, 141.76, 106.569984, 140.17, 103.469986, 140.73999);
                shape8.curveTo(103.37999, 137.76999, 105.02998, 133.66998, 101.64999, 131.93999);
                shape8.curveTo(101.19999, 127.959984, 101.8557, 123.15636, 102.71156, 120.46933);
                shape8.curveTo(103.99054, 119.71111, 105.61998, 119.52999, 107.23998, 119.37999);
                shape8.curveTo(109.36998, 115.02999, 105.05998, 110.83999, 100.97998, 110.12999);
                shape8.curveTo(98.959984, 109.28999, 95.139984, 109.77999, 95.39998, 106.67999);
                shape8.curveTo(93.22998, 106.17999, 91.05998, 105.649994, 88.89998, 105.079994);
                shape8.curveTo(89.529976, 104.579994, 90.15998, 104.079994, 90.79998, 103.59);
                shape8.curveTo(88.459984, 103.28, 84.59998, 102.32, 85.31998, 99.229996);
                shape8.curveTo(83.919975, 97.92, 82.51997, 96.6, 81.13998, 95.27);
                shape8.lineTo(84.059975, 95.46);
                shape8.curveTo(82.06998, 92.64, 79.61997, 88.84, 75.63998, 89.409996);
                shape8.curveTo(74.79998, 91.759995, 73.99998, 94.35999, 75.45998, 96.659996);
                shape8.curveTo(78.81998, 102.899994, 84.90997, 107.35999, 87.51997, 114.079994);
                shape8.curveTo(87.78997, 116.77, 87.54997, 119.46999, 87.42998, 122.159996);
                shape8.curveTo(85.449974, 122.24, 85.35998, 124.399994, 84.54998, 125.71999);
                shape8.curveTo(83.23998, 128.61, 80.35998, 130.68999, 77.19998, 131.0);
                shape8.curveTo(78.36998, 129.57, 79.429985, 128.04, 80.27998, 126.39);
                shape8.curveTo(79.569984, 126.52, 78.14999, 126.77, 77.43999, 126.9);
                shape8.curveTo(80.09999, 123.68, 82.59999, 117.96, 78.73999, 114.73);
                shape8.curveTo(78.73999, 115.87, 78.72999, 118.15, 78.72999, 119.29);
                shape8.curveTo(74.88999, 115.36, 76.59999, 109.9, 76.41999, 105.05);
                shape8.curveTo(74.91999, 100.36, 71.149994, 96.22, 71.81999, 90.98);
                shape8.closePath();
                g.fill(shape8);
            }

            private void paintShapeNode_0_0_9(Graphics2D g) {
                GeneralPath shape9 = new GeneralPath();
                shape9.moveTo(153.81, 87.91);
                shape9.curveTo(154.76892, 86.06477, 157.23999, 89.270004, 156.73, 90.73);
                shape9.curveTo(155.2273, 89.0601, 152.68405, 90.5169, 153.81, 87.91);
                shape9.closePath();
                g.fill(shape9);
            }

            private void paintShapeNode_0_0_10(Graphics2D g) {
                GeneralPath shape10 = new GeneralPath();
                shape10.moveTo(212.2355, 226.39409);
                shape10.curveTo(215.83551, 226.89409, 220.81264, 227.90776, 220.9155, 233.01817);
                shape10.curveTo(217.69978, 231.76959, 214.6435, 230.29216, 211.13676, 231.8783);
                g.fill(shape10);
            }

            private void paintShapeNode_0_0_11(Graphics2D g) {
                GeneralPath shape11 = new GeneralPath();
                shape11.moveTo(209.8299, 232.01428);
                shape11.curveTo(209.2899, 233.59428, 208.42, 233.03, 207.88, 234.63);
                shape11.lineTo(210.35034, 234.23967);
                shape11.curveTo(211.38, 234.05067, 211.85263, 238.87645, 210.47034, 239.33556);
                shape11.lineTo(209.4, 239.73);
                shape11.curveTo(209.93999, 242.75, 209.53998, 247.69, 205.23999, 246.26999);
                shape11.curveTo(201.43999, 245.09, 197.65999, 243.82999, 193.71999, 243.23);
                shape11.curveTo(193.84, 244.55, 195.19983, 244.97032, 195.31982, 246.29031);
                shape11.curveTo(192.09982, 245.57031, 187.79999, 247.03, 184.61998, 246.26991);
                shape11.curveTo(176.97998, 246.88991, 170.19998, 242.10991, 162.45998, 242.3099);
                shape11.curveTo(166.13997, 237.4899, 172.11998, 233.4899, 170.51997, 226.5899);
                shape11.curveTo(167.69997, 226.2699, 164.87997, 225.9899, 162.05997, 225.68991);
                shape11.curveTo(160.6154, 222.52452, 160.3572, 216.25626, 162.53659, 212.01123);
                shape11.lineTo(162.56099, 211.98022);
                shape11.curveTo(169.661, 208.66022, 177.21997, 206.56996, 185.17996, 208.40996);
                shape11.curveTo(185.89998, 209.50996, 187.33997, 211.70996, 188.05997, 212.80995);
                shape11.curveTo(186.41997, 219.74995, 182.13997, 226.50995, 184.27998, 233.84995);
                shape11.curveTo(191.43999, 234.22995, 198.15997, 231.80995, 204.23997, 228.24994);
                shape11.curveTo(206.45999, 226.66994, 208.30962, 226.05836, 210.80954, 226.30847);
                g.fill(shape11);
            }

            private void paintShapeNode_0_0_12(Graphics2D g) {
                GeneralPath shape12 = new GeneralPath();
                shape12.moveTo(149.55453, 118.05516);
                shape12.curveTo(149.86945, 117.09734, 149.76004, 116.69487, 149.51514, 115.52989);
                shape12.curveTo(151.81514, 115.58769, 154.56543, 116.04015, 153.44543, 119.35015);
                shape12.curveTo(152.2803, 118.12034, 151.29454, 117.895164, 149.55453, 118.05516);
                shape12.closePath();
                g.fill(shape12);
            }

            private void paintShapeNode_0_0_13(Graphics2D g) {
                GeneralPath shape13 = new GeneralPath();
                shape13.moveTo(148.93, 119.94);
                shape13.curveTo(151.84999, 119.770004, 152.37999, 122.54, 151.17, 124.670006);
                shape13.curveTo(150.50491, 123.30019, 149.44243, 122.750114, 147.90262, 122.22005);
                shape13.curveTo(148.56247, 121.63997, 148.94742, 120.92245, 148.93, 119.94001);
                shape13.closePath();
                g.fill(shape13);
            }

            private void paintShapeNode_0_0_14(Graphics2D g) {
                GeneralPath shape14 = new GeneralPath();
                shape14.moveTo(115.65469, 134.43674);
                shape14.curveTo(117.36469, 135.65674, 119.700005, 137.39001, 119.78001, 139.63);
                shape14.curveTo(118.7344, 138.8401, 117.73766, 138.23459, 116.2744, 138.45552);
                shape14.curveTo(116.4144, 137.24551, 116.2824, 135.76338, 115.6547, 134.43674);
                shape14.closePath();
                g.fill(shape14);
            }

            private void paintShapeNode_0_0_15(Graphics2D g) {
                GeneralPath shape15 = new GeneralPath();
                shape15.moveTo(113.15122, 139.31664);
                shape15.curveTo(114.36122, 140.82663, 115.23, 142.96, 113.47, 144.27);
                shape15.curveTo(113.18483, 142.55023, 112.53366, 141.81734, 110.818985, 141.04225);
                shape15.curveTo(111.691124, 140.75664, 112.43459, 140.45757, 113.15122, 139.31664);
                shape15.closePath();
                g.fill(shape15);
            }

            private void paintShapeNode_0_0_16(Graphics2D g) {
                GeneralPath shape16 = new GeneralPath();
                shape16.moveTo(105.24, 141.51);
                shape16.curveTo(106.552055, 141.16812, 107.303825, 141.0178, 108.61, 141.01);
                shape16.curveTo(108.92, 142.87, 108.08, 144.48, 107.0, 145.90999);
                shape16.curveTo(107.0302, 143.90456, 106.11853, 142.27802, 105.24, 141.51);
                shape16.closePath();
                g.fill(shape16);
            }

            private void paintShapeNode_0_0_17(Graphics2D g) {
                GeneralPath shape17 = new GeneralPath();
                shape17.moveTo(140.87395, 55.84454);
                shape17.curveTo(141.72786, 55.11712, 142.47102, 54.20929, 144.02596, 54.05161);
                shape17.curveTo(142.66954, 55.04943, 143.24257, 56.45207, 142.8594, 57.35818);
                shape17.curveTo(141.75546, 56.888355, 141.16772, 56.436935, 140.87395, 55.84454);
                shape17.closePath();
                g.fill(shape17);
            }

            private void paintCanvasGraphicsNode_0_0(Graphics2D g) {
                // _0_0_0
                AffineTransform trans_0_0_0 = g.getTransform();
                g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
                paintShapeNode_0_0_0(g);
                g.setTransform(trans_0_0_0);
                // _0_0_1
                AffineTransform trans_0_0_1 = g.getTransform();
                g.transform(new AffineTransform(0.800000011920929f, 0.0f, 0.0f, 0.800000011920929f, -4.999999873689376E-6f, 0.0f));
                paintShapeNode_0_0_1(g);
                g.setTransform(trans_0_0_1);
                // _0_0_2
                AffineTransform trans_0_0_2 = g.getTransform();
                g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
                paintShapeNode_0_0_2(g);
                g.setTransform(trans_0_0_2);
                // _0_0_3
                AffineTransform trans_0_0_3 = g.getTransform();
                g.transform(new AffineTransform(2.0f, 0.0f, 0.0f, 2.0f, -86.9000015258789f, 3.049999952316284f));
                paintShapeNode_0_0_3(g);
                g.setTransform(trans_0_0_3);
                // _0_0_4
                AffineTransform trans_0_0_4 = g.getTransform();
                g.transform(new AffineTransform(2.0f, 0.0f, 0.0f, 2.0f, -87.72472381591797f, 3.214944362640381f));
                paintShapeNode_0_0_4(g);
                g.setTransform(trans_0_0_4);
                // _0_0_5
                AffineTransform trans_0_0_5 = g.getTransform();
                g.transform(new AffineTransform(1.9944379329681396f, -0.14905503392219543f, 0.14905503392219543f, 1.9944379329681396f, -95.549072265625f, 23.755638122558594f));
                paintShapeNode_0_0_5(g);
                g.setTransform(trans_0_0_5);
                // _0_0_6
                AffineTransform trans_0_0_6 = g.getTransform();
                g.transform(new AffineTransform(2.0f, 0.0f, 0.0f, 2.0f, -86.74449157714844f, 2.505711317062378f));
                paintShapeNode_0_0_6(g);
                g.setTransform(trans_0_0_6);
                // _0_0_7
                AffineTransform trans_0_0_7 = g.getTransform();
                g.transform(new AffineTransform(2.0f, 0.0f, 0.0f, 2.0f, -86.9000015258789f, 3.049999952316284f));
                paintShapeNode_0_0_7(g);
                g.setTransform(trans_0_0_7);
                // _0_0_8
                AffineTransform trans_0_0_8 = g.getTransform();
                g.transform(new AffineTransform(2.0f, 0.0f, 0.0f, 2.0f, -86.9000015258789f, 3.049999952316284f));
                paintShapeNode_0_0_8(g);
                g.setTransform(trans_0_0_8);
                // _0_0_9
                AffineTransform trans_0_0_9 = g.getTransform();
                g.transform(new AffineTransform(1.9197989702224731f, -0.5606889128684998f, 0.5606889128684998f, 1.9197989702224731f, -124.46099853515625f, 96.35636901855469f));
                paintShapeNode_0_0_9(g);
                g.setTransform(trans_0_0_9);
                // _0_0_10
                AffineTransform trans_0_0_10 = g.getTransform();
                g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
                paintShapeNode_0_0_10(g);
                g.setTransform(trans_0_0_10);
                // _0_0_11
                AffineTransform trans_0_0_11 = g.getTransform();
                g.transform(new AffineTransform(1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f));
                paintShapeNode_0_0_11(g);
                g.setTransform(trans_0_0_11);
                // _0_0_12
                AffineTransform trans_0_0_12 = g.getTransform();
                g.transform(new AffineTransform(2.0f, 0.0f, 0.0f, 2.0f, -86.9000015258789f, 3.049999952316284f));
                paintShapeNode_0_0_12(g);
                g.setTransform(trans_0_0_12);
                // _0_0_13
                AffineTransform trans_0_0_13 = g.getTransform();
                g.transform(new AffineTransform(1.9810659885406494f, -0.2745497226715088f, 0.2745497226715088f, 1.9810659885406494f, -117.09091186523438f, 45.624305725097656f));
                paintShapeNode_0_0_13(g);
                g.setTransform(trans_0_0_13);
                // _0_0_14
                AffineTransform trans_0_0_14 = g.getTransform();
                g.transform(new AffineTransform(2.0f, 0.0f, 0.0f, 2.0f, -86.64730072021484f, 2.9722445011138916f));
                paintShapeNode_0_0_14(g);
                g.setTransform(trans_0_0_14);
                // _0_0_15
                AffineTransform trans_0_0_15 = g.getTransform();
                g.transform(new AffineTransform(2.0f, 0.0f, 0.0f, 2.0f, -86.9000015258789f, 3.049999952316284f));
                paintShapeNode_0_0_15(g);
                g.setTransform(trans_0_0_15);
                // _0_0_16
                AffineTransform trans_0_0_16 = g.getTransform();
                g.transform(new AffineTransform(1.9861799478530884f, 0.2347116470336914f, -0.2347116470336914f, 1.9861799478530884f, -55.60910415649414f, -19.701990127563477f));
                paintShapeNode_0_0_16(g);
                g.setTransform(trans_0_0_16);
                // _0_0_17
                AffineTransform trans_0_0_17 = g.getTransform();
                g.transform(new AffineTransform(1.8588589429855347f, 0.737999677658081f, -0.737999677658081f, 1.8588589429855347f, -19.666950225830078f, -88.41539764404297f));
                paintShapeNode_0_0_17(g);
                g.setTransform(trans_0_0_17);
            }

            private void paintRootGraphicsNode_0(Graphics2D g, AffineTransform transform) {
                AffineTransform trans_0_0 = g.getTransform();
                g.transform(transform);
                paintCanvasGraphicsNode_0_0(g);
                g.setTransform(trans_0_0);
            }

            public boolean setup(Graphics2D g, UIComponent component) {
                return UIFace.getRenderProcedure(UIClosedShape.class).setup(g, component);
            }

            public void draw(Graphics2D g, UIComponent component) {
                GriffinLogo logo = (GriffinLogo) component;

                Point pos = logo.getPos();
                Dimension size = logo.size;
                logo.transform.setTransform(size.width / 268.f, 0.f, 0.f, size.height / 371.f, pos.x, pos.y);

                paintRootGraphicsNode_0(g, logo.transform);

            }
        });
    }

    public GriffinLogo() {
        super(GriffinLogo.class);
        setSize(268, 371);
        transform = new AffineTransform(1.f, 0.f, 0.f, 1.f, 0, 0);
    }
}
