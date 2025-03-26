import {
  Alarm,
  InfoCircle,
  Notification,
  CloseCircle,
  MoreCircle,
  ProfileCircle,
  TickCircle,
  ArrowCircleUp,
  ArrowCircleDown,
  ArrowCircleLeft,
  ArrowCircleRight,
  ArrowCircleUp2,
  ArrowCircleDown2,
  ArrowCircleLeft2,
  ArrowCircleRight2,
  ArrowLeft,
  ArrowRight,
  ArrowUp,
  ArrowDown,
  ArrowLeft2,
  ArrowRight2,
  ArrowUp2,
  ArrowDown2,
  ArrowSquareRight,
  ArrowSquareLeft,
  ArrowSquareUp,
  ArrowSquareDown,
  Filter,
  Setting4,
  AddCircle,
  TaskSquare,
  Shop,
  ShoppingCart,
  DollarSquare,
  ChartSquare,
  Wallet,
  Briefcase,
  Home2,
  Personalcard,
  Profile2User,
  Trash,
  Edit,
  GalleryAdd,
  MinusSquare,
  MinusCirlce,
  Edit2,
  SearchNormal1,
  HambergerMenu,
  Add,
  More,
} from 'iconsax-react';
import { HiStar } from "react-icons/hi2";

type CustomColor =
  | 'white'
  | 'broken-white'
  | 'new-grey'
  | 'grey-25'
  | 'grey-30'
  | 'light-grey'
  | 'grey'
  | 'dark-grey'
  | 'black'
  | 'light-cyan'
  | 'light-cyan-30'
  | 'purple'
  | 'purple-30'
  | 'yellow'
  | 'yellow-30'
  | 'success'
  | 'danger'
  | 'info'
  | 'warning'
  | 'dark-purple'
  | 'dark-pink'
  | 'pink'
  | 'pink-30'
  | 'success-30'
  | 'placeholder';

// Icon name mapping
const ICONSAX_ICONS = {
  Alarm,
  InfoCircle,
  Notification,
  CloseCircle,
  MoreCircle,
  ProfileCircle,
  TickCircle,
  ArrowCircleUp,
  ArrowCircleDown,
  ArrowCircleLeft,
  ArrowCircleRight,
  ArrowCircleUp2,
  ArrowCircleDown2,
  ArrowCircleLeft2,
  ArrowCircleRight2,
  ArrowLeft,
  ArrowRight,
  ArrowUp,
  ArrowDown,
  ArrowLeft2,
  ArrowRight2,
  ArrowUp2,
  ArrowDown2,
  ArrowSquareRight,
  ArrowSquareLeft,
  ArrowSquareUp,
  ArrowSquareDown,
  Filter,
  Setting4,
  AddCircle,
  TaskSquare,
  Shop,
  ShoppingCart,
  DollarSquare,
  ChartSquare,
  Wallet,
  Briefcase,
  Home2,
  Personalcard,
  Profile2User,
  Trash,
  Edit,
  GalleryAdd,
  MinusSquare,
  MinusCirlce,
  Edit2,
  SearchNormal1,
  HambergerMenu,
  Star: HiStar,
  Add,
  More,
  Close: Add,
  CircleEdit: Add,
  CircleTrash: Add,
  CircleAdd: Add,
} as const;

type IconsaxIconName = keyof typeof ICONSAX_ICONS;

interface IconProps {
  name: IconsaxIconName;
  size?: number;
  color?: CustomColor | string;
  variant?: 'Linear' | 'Outline' | 'Broken' | 'Bold' | 'Bulk' | 'TwoTone';
  className?: string;
}

export const Icon = ({
  name,
  size = 24,
  color = 'currentColor',
  variant = 'Linear',
  className = '',
}: IconProps) => {
  const IconComponent = ICONSAX_ICONS[name];
  const isCircleIcon = name === 'CircleEdit' || name === 'CircleTrash' || name === 'CircleAdd';

  // Convert color name to CSS variable if it's a CustomColor
  const getColor = (colorName: string) => {
    return colorName.startsWith('var(--') ? colorName : `var(--color-${colorName})`;
  };

  if (isCircleIcon) {
    return (
      <div className="relative inline-flex items-center justify-center">
        <div
          className={`${className} rounded-full`}
          style={{
            width: size,
            height: size,
            backgroundColor: '#FFE999',
          }}
        />
        {name === 'CircleEdit' ? (
          <Edit2
            size={size * 0.6}
            color={color === 'currentColor' ? color : getColor(color)}
            variant={variant}
            className="absolute"
          />
        ) : name === 'CircleTrash' ? (
          <Trash
            size={size * 0.65}
            color={color === 'currentColor' ? color : getColor(color)}
            variant={variant}
            className="absolute"
          />
        ) : (
          <Add
            size={size * 0.8}
            color={color === 'currentColor' ? color : getColor(color)}
            variant={variant}
            className="absolute"
          />
        )}
      </div>
    );
  }

  // Star 아이콘일 경우 기본 색상을 yellow로 설정
  if (name === 'Star' && color === 'currentColor') {
    color = 'yellow';
  }

  return (
    <IconComponent
      size={size}
      color={color === 'currentColor' ? color : getColor(color)}
      variant={variant}
      className={`${className} ${name === 'Close' ? 'rotate-45' : ''}`}
    />
  );
};

export type { IconProps, IconsaxIconName, CustomColor };
