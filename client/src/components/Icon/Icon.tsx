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
  Star1,
  Add,
  More,
} from 'iconsax-react';

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
  Star: Star1,
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
  color?: string;
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
            color={color}
            variant={variant}
            className="absolute"
          />
        ) : name === 'CircleTrash' ? (
          <Trash
            size={size * 0.65}
            color={color}
            variant={variant}
            className="absolute"
          />
        ) : (
          <Add
            size={size * 0.8}
            color={color}
            variant={variant}
            className="absolute"
          />
        )}
      </div>
    );
  }

  return (
    <IconComponent
      size={size}
      color={color}
      variant={variant}
      className={`${className} ${name === 'Close' ? 'rotate-45' : ''}`}
    />
  );
};

export type { IconProps, IconsaxIconName };
