USE [Numerology]
GO
/****** Object:  Table [dbo].[__EFMigrationsHistory]    Script Date: 6/3/2025 9:25:34 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[__EFMigrationsHistory](
	[MigrationId] [nvarchar](150) NOT NULL,
	[ProductVersion] [nvarchar](32) NOT NULL,
 CONSTRAINT [PK___EFMigrationsHistory] PRIMARY KEY CLUSTERED 
(
	[MigrationId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BetResults]    Script Date: 6/3/2025 9:25:34 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BetResults](
	[BetResultId] [int] IDENTITY(1,1) NOT NULL,
	[BetId] [int] NOT NULL,
	[ResultDate] [date] NOT NULL,
	[IsWin] [bit] NULL,
	[WinAmount] [bigint] NULL,
	[CheckedAt] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[BetResultId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
 CONSTRAINT [UQ_BetResult] UNIQUE NONCLUSTERED 
(
	[BetId] ASC,
	[ResultDate] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Bets]    Script Date: 6/3/2025 9:25:34 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Bets](
	[BetId] [int] IDENTITY(1,1) NOT NULL,
	[UserId] [int] NOT NULL,
	[BetDate] [date] NOT NULL,
	[NumberType] [varchar](10) NOT NULL,
	[Number] [char](2) NOT NULL,
	[Amount] [bigint] NOT NULL,
	[Region] [varchar](5) NULL,
PRIMARY KEY CLUSTERED 
(
	[BetId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[LotteryResults]    Script Date: 6/3/2025 9:25:34 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LotteryResults](
	[ResultId] [int] IDENTITY(1,1) NOT NULL,
	[ResultDate] [date] NOT NULL,
	[SpecialPrize] [char](6) NOT NULL,
	[PrizeList] [nvarchar](max) NULL,
	[Region] [varchar](5) NULL,
	[CreatedAt] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[ResultId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
UNIQUE NONCLUSTERED 
(
	[ResultDate] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Users]    Script Date: 6/3/2025 9:25:34 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Users](
	[UserId] [int] IDENTITY(1,1) NOT NULL,
	[Username] [varchar](50) NOT NULL,
	[Password] [varchar](255) NOT NULL,
	[FullName] [varchar](100) NULL,
	[Phone] [varchar](15) NULL,
	[Gender] [bit] NULL,
	[CreatedAt] [datetime] NULL,
	[Role] [varchar](20) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[UserId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
UNIQUE NONCLUSTERED 
(
	[Username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[BetResults] ADD  DEFAULT ((0)) FOR [IsWin]
GO
ALTER TABLE [dbo].[BetResults] ADD  DEFAULT ((0)) FOR [WinAmount]
GO
ALTER TABLE [dbo].[BetResults] ADD  DEFAULT (getdate()) FOR [CheckedAt]
GO
ALTER TABLE [dbo].[Bets] ADD  DEFAULT ('MB') FOR [Region]
GO
ALTER TABLE [dbo].[LotteryResults] ADD  DEFAULT ('MB') FOR [Region]
GO
ALTER TABLE [dbo].[LotteryResults] ADD  DEFAULT (getdate()) FOR [CreatedAt]
GO
ALTER TABLE [dbo].[Users] ADD  DEFAULT (getdate()) FOR [CreatedAt]
GO
ALTER TABLE [dbo].[Users] ADD  DEFAULT ('user') FOR [Role]
GO
ALTER TABLE [dbo].[BetResults]  WITH CHECK ADD FOREIGN KEY([BetId])
REFERENCES [dbo].[Bets] ([BetId])
GO
ALTER TABLE [dbo].[Bets]  WITH CHECK ADD FOREIGN KEY([UserId])
REFERENCES [dbo].[Users] ([UserId])
GO
ALTER TABLE [dbo].[Bets]  WITH CHECK ADD  CONSTRAINT [CK_Bets_NumberType] CHECK  (([NumberType]='de' OR [NumberType]='lo'))
GO
ALTER TABLE [dbo].[Bets] CHECK CONSTRAINT [CK_Bets_NumberType]
GO
ALTER TABLE [dbo].[Bets]  WITH CHECK ADD  CONSTRAINT [CK_Bets_Region] CHECK  (([Region]='MT' OR [Region]='MN' OR [Region]='MB'))
GO
ALTER TABLE [dbo].[Bets] CHECK CONSTRAINT [CK_Bets_Region]
GO
ALTER TABLE [dbo].[LotteryResults]  WITH CHECK ADD  CONSTRAINT [CK_LotteryResults_Region] CHECK  (([Region]='MT' OR [Region]='MN' OR [Region]='MB'))
GO
ALTER TABLE [dbo].[LotteryResults] CHECK CONSTRAINT [CK_LotteryResults_Region]
GO
ALTER TABLE [dbo].[Users]  WITH CHECK ADD  CONSTRAINT [CK_Users_Role] CHECK  (([Role]='general-agent' OR [Role]='sub-agent' OR [Role]='user'))
GO
ALTER TABLE [dbo].[Users] CHECK CONSTRAINT [CK_Users_Role]
GO
